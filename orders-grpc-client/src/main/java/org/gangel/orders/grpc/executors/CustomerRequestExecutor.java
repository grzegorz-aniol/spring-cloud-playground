package org.gangel.orders.grpc.executors;

import io.grpc.stub.StreamObserver;
import org.gangel.jperfstat.Histogram;
import org.gangel.orders.proto.CustomerServiceGrpc;
import org.gangel.orders.proto.CustomerServiceGrpc.CustomerServiceStub;
import org.gangel.orders.proto.NewCustomerRequest;
import org.gangel.orders.proto.NewCustomerResponse;

import java.util.concurrent.Callable;

public abstract class CustomerRequestExecutor<
        R extends com.google.protobuf.GeneratedMessageV3, 
        O extends com.google.protobuf.GeneratedMessageV3> 
    extends AbstractGrpcAsyncExecutor<CustomerServiceStub, R, O>  {

    public CustomerRequestExecutor() {
        super(channel -> CustomerServiceGrpc.newStub(channel));
    }

    public static Callable<Histogram> newCustomers() {
        return new CustomerRequestExecutor<NewCustomerRequest, NewCustomerResponse>() {
            @Override
            protected StreamObserver<NewCustomerRequest> getRpcCall(CustomerServiceStub stub,
                    StreamObserver<NewCustomerResponse> response) {
                return stub.createNewCustomers(response);
            }
            @Override
            protected NewCustomerRequest produceRequest() {
                return NewCustomerRequest.newBuilder()
                        .setCustomer(CustomerProducer.produce())
                        .build();
            }
        };
    }
}
