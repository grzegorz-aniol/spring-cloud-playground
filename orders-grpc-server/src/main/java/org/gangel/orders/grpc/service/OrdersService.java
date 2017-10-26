package org.gangel.orders.grpc.service;

import io.grpc.stub.StreamObserver;
import org.gangel.orders.proto.Customer;
import org.gangel.orders.proto.GetCustomerRequest;
import org.gangel.orders.proto.GetCustomerResponse;
import org.gangel.orders.proto.NewCustomerRequest;
import org.gangel.orders.proto.NewCustomerResponse;
import org.gangel.orders.proto.OrdersServiceGrpc.OrdersServiceImplBase;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class OrdersService extends OrdersServiceImplBase  {

    @Override
    public void createNewCustomer(NewCustomerRequest request,
            StreamObserver<NewCustomerResponse> responseObserver) {
        responseObserver.onNext(NewCustomerResponse.newBuilder().setId(1).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getCustomer(GetCustomerRequest request,
            StreamObserver<GetCustomerResponse> responseObserver) {
        responseObserver.onNext(GetCustomerResponse.newBuilder()
                .setCustomer(Customer.newBuilder()
                        .setId(1)
                        .setName("Mike")
                        .setLastname("Pepper")
                        .build())
                .build()
        );
        responseObserver.onCompleted();
    }

}
