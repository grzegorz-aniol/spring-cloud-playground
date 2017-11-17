package org.gangel.orders.grpc;

import com.google.protobuf.GeneratedMessageV3;
import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import org.gangel.orders.common.GlobalExceptionHandler;
import org.gangel.orders.proto.OrdersServiceGrpc;
import org.gangel.orders.proto.OrdersServiceGrpc.OrdersServiceFutureStub;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

class RequestTask implements Callable<Long> {
    
    private Function<OrdersServiceFutureStub, ? extends com.google.protobuf.GeneratedMessageV3> requestExecutor; 

    public RequestTask(Function<OrdersServiceFutureStub, ? extends com.google.protobuf.GeneratedMessageV3> requestExecutor) {
        this.requestExecutor = requestExecutor; 
    }
    
    @Override
    public Long call() throws Exception {
        GlobalExceptionHandler.register();

        final ManagedChannel channel = NettyChannelBuilder.forAddress(Configuration.host, Configuration.port)
                    .sslContext(GrpcSslContexts.forClient()
                            .trustManager(new File(Configuration.certFilePath))
                            .build())
                    .build();
                
        final OrdersServiceFutureStub stub = OrdersServiceGrpc.newFutureStub(channel);
        
        long totalTime = 0;        
        
        for (long i=0; i < Configuration.numOfIterations; ++i) {
            try {
                long t0 = System.currentTimeMillis();
                GeneratedMessageV3 response = requestExecutor.apply(stub);
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