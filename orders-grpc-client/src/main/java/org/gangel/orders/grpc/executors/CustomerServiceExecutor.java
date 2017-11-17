package org.gangel.orders.grpc.executors;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.GeneratedMessageV3;
import io.grpc.ManagedChannel;
import org.gangel.orders.proto.Customer;
import org.gangel.orders.proto.CustomerServiceGrpc;
import org.gangel.orders.proto.CustomerServiceGrpc.CustomerServiceFutureStub;
import org.gangel.orders.proto.NewCustomerRequest;

import java.util.function.Function;

public class CustomerServiceExecutor extends GrpcServiceExecutor<CustomerServiceFutureStub> {

    public CustomerServiceExecutor(Function<CustomerServiceFutureStub, ListenableFuture<? extends GeneratedMessageV3>> requestFunction) {
        super(requestFunction);
    }

    @Override
    protected CustomerServiceFutureStub produceStub(ManagedChannel channel) {
        return CustomerServiceGrpc.newFutureStub(channel);
    }
    
    public static CustomerServiceExecutor getNewCustomerRequestExecutor() {
        return new CustomerServiceExecutor((stub) -> {
            return stub.createNewCustomer(NewCustomerRequest.newBuilder()
                    .setCustomer(Customer.newBuilder()
                            .setName("Test1")
                            .setLastname("LastName")
                            .build())
                    .build());
        });
    }

}
