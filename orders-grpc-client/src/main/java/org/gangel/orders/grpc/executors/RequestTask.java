package org.gangel.orders.grpc.executors;

import com.google.protobuf.GeneratedMessageV3;
import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import org.gangel.jperfstat.Histogram;
import org.gangel.orders.grpc.common.GlobalExceptionHandler;
import org.gangel.orders.job.Configuration;
import org.gangel.orders.proto.OrdersServiceGrpc;
import org.gangel.orders.proto.OrdersServiceGrpc.OrdersServiceFutureStub;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

class RequestTask implements Callable<Histogram> {
    
    private Function<OrdersServiceFutureStub, ? extends com.google.protobuf.GeneratedMessageV3> requestExecutor; 

    public RequestTask(Function<OrdersServiceFutureStub, ? extends com.google.protobuf.GeneratedMessageV3> requestExecutor) {
        this.requestExecutor = requestExecutor; 
    }
    
    @Override
    public Histogram call() throws Exception {
        GlobalExceptionHandler.register();
        Histogram histogram = new Histogram(Configuration.numOfIterations);

        final ManagedChannel channel = NettyChannelBuilder.forAddress(Configuration.host, Configuration.port)
                    .sslContext(GrpcSslContexts.forClient()
                            .trustManager(new File(Configuration.certFilePath))
                            .build())
                    .build();
                
        final OrdersServiceFutureStub stub = OrdersServiceGrpc.newFutureStub(channel);
        
        for (long i=0; i < Configuration.numOfIterations; ++i) {
            try {
                long t0 = System.nanoTime();
                GeneratedMessageV3 response = requestExecutor.apply(stub);
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