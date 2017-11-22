package org.gangel.orders.grpc.executors;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import org.gangel.orders.grpc.Configuration;
import org.gangel.orders.grpc.common.GlobalExceptionHandler;
import org.gangel.orders.grpc.common.Histogram;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public abstract class AbstractGrpcServiceExecutor<S> implements Callable<Histogram> {
    
    private Histogram histogram;

    private Function<S, ListenableFuture<? extends GeneratedMessageV3>> requestFunction;

    protected AbstractGrpcServiceExecutor(Function<S, ListenableFuture<? extends GeneratedMessageV3> > requestFunction) {
        this.requestFunction = requestFunction;
    }
    
    protected abstract S produceStub(final ManagedChannel channel);
    
    public Histogram getHistogram() {
        return this.histogram;
    }
    
    public Histogram call() throws Exception {
        
        GlobalExceptionHandler.register();
        histogram = new Histogram(Configuration.numOfIterations);

        final ManagedChannel channel = NettyChannelBuilder
                .forAddress(Configuration.host, Configuration.port).sslContext(GrpcSslContexts
                        .forClient().trustManager(new File(Configuration.certFilePath)).build())
                .build();
        final S stub = produceStub(channel);

        for (long i = 0; i < Configuration.numOfIterations; ++i) {
            try {
                long t0 = System.nanoTime();
                
                ListenableFuture<? extends Message> future = requestFunction.apply(stub);
                Message response;
                try {
                    response = future.get(5, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    throw new RuntimeException(e);
                }                
                
                long time = (System.nanoTime() - t0);
                if (response == null) {
                    throw new RuntimeException("Wrong response: null object");
                }
                histogram.put(time);
                
            } catch (Throwable e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                return null;
            }
        }

        channel.shutdown();
        channel.awaitTermination(5, TimeUnit.SECONDS);

        return histogram;
    }

}
