package org.gangel.orders.grpc.service;

import io.grpc.stub.StreamObserver;
import org.gangel.orders.proto.Customer;
import org.gangel.orders.proto.GetCustomerRequest;
import org.gangel.orders.proto.GetCustomerResponse;
import org.gangel.orders.proto.GetOrderRequest;
import org.gangel.orders.proto.GetOrderResponse;
import org.gangel.orders.proto.GetProductRequest;
import org.gangel.orders.proto.GetProductResponse;
import org.gangel.orders.proto.NewCustomerRequest;
import org.gangel.orders.proto.NewCustomerResponse;
import org.gangel.orders.proto.NewOrderRequest;
import org.gangel.orders.proto.NewOrderResponse;
import org.gangel.orders.proto.NewProductRequest;
import org.gangel.orders.proto.NewProductResponse;
import org.gangel.orders.proto.OrdersServiceGrpc.OrdersServiceImplBase;
import org.gangel.orders.proto.PingRequest;
import org.gangel.orders.proto.PingResponse;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@GRpcService
@Component
public class OrdersService extends OrdersServiceImplBase  {

    @Value("${grpc.port}")
    private int instancePort;    
    
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

    @Override
    public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
        responseObserver.onNext(PingResponse.newBuilder().setResponse(Integer.toString(instancePort)).build());
        responseObserver.onCompleted();
    }

    @Override
    public void createNewProduct(NewProductRequest request,
            StreamObserver<NewProductResponse> responseObserver) {
        // TODO Auto-generated method stub
        super.createNewProduct(request, responseObserver);
    }

    @Override
    public void getProduct(GetProductRequest request,
            StreamObserver<GetProductResponse> responseObserver) {
        // TODO Auto-generated method stub
        super.getProduct(request, responseObserver);
    }

    @Override
    public void addNewOrder(NewOrderRequest request,
            StreamObserver<NewOrderResponse> responseObserver) {
        // TODO Auto-generated method stub
        super.addNewOrder(request, responseObserver);
    }

    @Override
    public void getOrder(GetOrderRequest request,
            StreamObserver<GetOrderResponse> responseObserver) {
        // TODO Auto-generated method stub
        super.getOrder(request, responseObserver);
    }

}
