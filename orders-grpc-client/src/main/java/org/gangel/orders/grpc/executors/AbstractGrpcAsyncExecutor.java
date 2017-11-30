package org.gangel.orders.grpc.executors;

import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.gangel.jperfstat.Histogram;
import org.gangel.orders.grpc.common.GlobalExceptionHandler;
import org.gangel.orders.job.Configuration;

import java.io.File;
import java.util.Deque;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public abstract class AbstractGrpcAsyncExecutor<S extends io.grpc.stub.AbstractStub<?>, 
        R extends com.google.protobuf.GeneratedMessageV3, 
        O extends com.google.protobuf.GeneratedMessageV3> 
    implements Callable<Histogram> {

    private final Histogram histogram = new Histogram(Configuration.numOfIterations);
    
    private Function<ManagedChannel, S> stubFuctory; 
    
    public AbstractGrpcAsyncExecutor(Function<ManagedChannel, S> stubFactory) {
        this.stubFuctory = stubFactory; 
    }
    
    protected abstract StreamObserver<R> getRpcCall(S stub, StreamObserver<O> response);
    
    protected abstract R produceRequest();
    
    @Override
    public Histogram call() throws Exception {
        GlobalExceptionHandler.register();

        final ManagedChannel channel = NettyChannelBuilder
                .forAddress(Configuration.host, Configuration.port).sslContext(GrpcSslContexts
                        .forClient().trustManager(new File(Configuration.certFilePath)).build())
                .build();
        
        if (Configuration.numOfWarmIterations > 0) {
            proceedRequests(channel, Configuration.numOfWarmIterations, true);            
        }

        histogram.setStartTime();
        proceedRequests(channel, Configuration.numOfIterations, false);            
        histogram.setStopTime();

        channel.shutdown();
        channel.awaitTermination(5, TimeUnit.SECONDS);

        return histogram;    
    }
    
    protected void proceedRequests(ManagedChannel channel, long count, boolean isWarming) {
        
        final AtomicLong activeRequests = new AtomicLong(0);    // current number of active requests - should be aprox. equals to initial value for semaphore 'readyToSend'
        final Deque<Long> startTime = new ConcurrentLinkedDeque<>(); // measure start time of request sending 
        final CountDownLatch waitForFinishAll = new CountDownLatch((int)count); // count down latch for all responses
        final Semaphore readyToSend = new Semaphore(1); // max concurrent requests to send before waiting for response
        
        // CustomerServiceGrpc.newStub(channel)
        // io.grpc.stub.AbstractStub
        S stub = stubFuctory.apply(channel);
        
        StreamObserver<R> requestObserver = getRpcCall(stub, new StreamObserver<O>() {

            @Override
            public void onNext(O value) {
                // save time
                long requestTime = System.nanoTime() - startTime.pollLast();
                // allow to send next request
                readyToSend.release();
                // track performance sample
                if (!isWarming) {
                    histogram.put(requestTime);
                }
                // decrease number of active requests
                activeRequests.decrementAndGet();
                // decrease total number of responses to wait
                waitForFinishAll.countDown();
            }

            @Override
            public void onError(Throwable t) {
                throw new RuntimeException(t);
            }

            @Override
            public void onCompleted() {
            }
            
        });        
        
        for (int i=0; i<count; ++i) {
            try {
                if (!readyToSend.tryAcquire(500, TimeUnit.SECONDS)) {
                    throw new RuntimeException("Can't send new request. Waiting timeout!");
                }
            } catch (InterruptedException e) {
                // nop
            }
            activeRequests.getAndIncrement();
            R request = produceRequest();
            startTime.addFirst(System.nanoTime());
            requestObserver.onNext(request);
        }
        
        requestObserver.onCompleted();
        
        // estimate pending requests and the timeout 
        long waitEstimation = activeRequests.get() * 200; // max 200 ms per each call
        try {
            if (!waitForFinishAll.await(waitEstimation, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Timeout for waiting on completing the job has occured! Waiting for: " + activeRequests.get());
            }
        } catch (InterruptedException e) {
        }
    }     
    
    
}
