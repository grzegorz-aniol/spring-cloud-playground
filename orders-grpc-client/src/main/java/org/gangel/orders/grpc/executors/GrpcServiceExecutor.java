package org.gangel.orders.grpc.executors;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import org.gangel.orders.common.GlobalExceptionHandler;
import org.gangel.orders.grpc.Configuration;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public abstract class GrpcServiceExecutor<S> implements Callable<Long> {

    private Function<S, ListenableFuture<? extends GeneratedMessageV3>> requestFunction;

    protected GrpcServiceExecutor(Function<S, ListenableFuture<? extends GeneratedMessageV3> > requestFunction) {
        this.requestFunction = requestFunction;
    }
    
    protected abstract S produceStub(final ManagedChannel channel);
    
    public Long call() throws Exception {
        
        GlobalExceptionHandler.register();

        final ManagedChannel channel = NettyChannelBuilder
                .forAddress(Configuration.host, Configuration.port).sslContext(GrpcSslContexts
                        .forClient().trustManager(new File(Configuration.certFilePath)).build())
                .build();
        final S stub = produceStub(channel);

        long totalTime = 0;

        for (long i = 0; i < Configuration.numOfIterations; ++i) {
            try {
                long t0 = System.currentTimeMillis();
                
                ListenableFuture<? extends Message> future = requestFunction.apply(stub);
                Message response;
                try {
                    response = future.get(5, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    throw new RuntimeException(e);
                }                
                
                totalTime += (System.currentTimeMillis() - t0);
                if (response == null) {
                    throw new RuntimeException("Wrong response: null object");
                }
            } catch (Throwable e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                return 0L;
            }
        }

        channel.shutdown();
        channel.awaitTermination(5, TimeUnit.SECONDS);

        return totalTime;
    }

}
