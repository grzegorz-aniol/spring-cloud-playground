package org.gangel.orders.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.gangel.orders.proto.Customer;
import org.gangel.orders.proto.NewCustomerRequest;
import org.gangel.orders.proto.NewCustomerResponse;
import org.gangel.orders.proto.OrdersServiceGrpc;
import org.gangel.orders.proto.OrdersServiceGrpc.OrdersServiceFutureStub;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class OrdersGRpcClientApp {

    public static void main(String[] args) {
        final ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .build();
        final OrdersServiceFutureStub stub = OrdersServiceGrpc.newFutureStub(channel);
        
        NewCustomerRequest request = NewCustomerRequest.newBuilder()
            .setCustomer(Customer.newBuilder()
                    .setId(1)
                    .build())
            .build();
        
        try {
            ListenableFuture<NewCustomerResponse> future = stub.createNewCustomer(request);
            NewCustomerResponse response = future.get(1, TimeUnit.SECONDS);
            System.out.println("Object ID = " + response.getId());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        System.out.println("Done.");
    }
}
