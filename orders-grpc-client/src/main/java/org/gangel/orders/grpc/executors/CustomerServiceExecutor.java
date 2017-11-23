package org.gangel.orders.grpc.executors;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.GeneratedMessageV3;
import io.grpc.ManagedChannel;
import io.netty.util.internal.ThreadLocalRandom;
import org.gangel.jperfstat.Histogram;
import org.gangel.orders.job.Configuration;
import org.gangel.orders.proto.Customer;
import org.gangel.orders.proto.CustomerServiceGrpc;
import org.gangel.orders.proto.CustomerServiceGrpc.CustomerServiceFutureStub;
import org.gangel.orders.proto.GetCustomerRequest;
import org.gangel.orders.proto.NewCustomerRequest;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class CustomerServiceExecutor extends AbstractGrpcServiceExecutor<CustomerServiceFutureStub> {

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
                            .setName(UUID.randomUUID().toString().substring(0, 10))
                            .setLastname(UUID.randomUUID().toString().substring(0,10))
                            .setEmail("test@domain.com")
                            .build())
                    .build());
        });
    }

    public static Callable<Histogram> getGetCustomerRequestExecutor() {
        return new CustomerServiceExecutor((stub) -> {
            return stub.getCustomer(GetCustomerRequest.newBuilder()
                    .setId(ThreadLocalRandom.current()
                            .nextInt(Configuration.minCustomerId, Configuration.maxCustomerId))
                    .build());
        });
    }

}
