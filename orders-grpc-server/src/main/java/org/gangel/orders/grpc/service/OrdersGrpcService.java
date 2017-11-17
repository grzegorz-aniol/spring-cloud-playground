package org.gangel.orders.grpc.service;

import io.grpc.stub.StreamObserver;
import org.gangel.orders.proto.GetOrderRequest;
import org.gangel.orders.proto.GetOrderResponse;
import org.gangel.orders.proto.NewOrderRequest;
import org.gangel.orders.proto.NewOrderResponse;
import org.gangel.orders.proto.OrdersServiceGrpc.OrdersServiceImplBase;
import org.gangel.orders.proto.PingRequest;
import org.gangel.orders.proto.PingResponse;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@GRpcService
@Component
public class OrdersGrpcService extends OrdersServiceImplBase  {

    @Value("${grpc.port}")
    private int instancePort;    
    
    @Override
    public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
        responseObserver.onNext(PingResponse.newBuilder().setResponse(Integer.toString(instancePort)).build());
        responseObserver.onCompleted();
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
