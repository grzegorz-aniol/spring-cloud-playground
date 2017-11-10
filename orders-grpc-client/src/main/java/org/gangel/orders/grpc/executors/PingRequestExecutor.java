package org.gangel.orders.grpc.executors;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.GeneratedMessageV3;
import org.gangel.orders.proto.OrdersServiceGrpc.OrdersServiceFutureStub;
import org.gangel.orders.proto.PingRequest;
import org.gangel.orders.proto.PingResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class PingRequestExecutor
        implements Function<OrdersServiceFutureStub, com.google.protobuf.GeneratedMessageV3> {

    @Override
    public GeneratedMessageV3 apply(OrdersServiceFutureStub stub) {
        ListenableFuture<PingResponse> future = stub.ping( PingRequest.newBuilder().build() );
        PingResponse response = null; 
        try {
            response = future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}

