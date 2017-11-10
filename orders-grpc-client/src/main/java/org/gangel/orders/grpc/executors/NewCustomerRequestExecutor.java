package org.gangel.orders.grpc.executors;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.GeneratedMessageV3;
import org.gangel.orders.proto.Customer;
import org.gangel.orders.proto.NewCustomerRequest;
import org.gangel.orders.proto.NewCustomerResponse;
import org.gangel.orders.proto.OrdersServiceGrpc.OrdersServiceFutureStub;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class NewCustomerRequestExecutor
        implements Function<OrdersServiceFutureStub, com.google.protobuf.GeneratedMessageV3> {

    @Override
    public GeneratedMessageV3 apply(OrdersServiceFutureStub stub) {
        NewCustomerRequest request = NewCustomerRequest.newBuilder()
                .setCustomer(Customer.newBuilder().setId(1).build()).build();
        ListenableFuture<NewCustomerResponse> future = stub.createNewCustomer(request);
        NewCustomerResponse response;
        try {
            response = future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return response;
    }


}
