package org.gangel.orders.grpc.service;

import io.grpc.stub.StreamObserver;
import org.gangel.orders.grpc.service.data.OrdersDataService;
import org.gangel.orders.proto.GetOrderRequest;
import org.gangel.orders.proto.GetOrderResponse;
import org.gangel.orders.proto.NewOrderRequest;
import org.gangel.orders.proto.NewOrderResponse;
import org.gangel.orders.proto.Orders;
import org.gangel.orders.proto.OrdersServiceGrpc.OrdersServiceImplBase;
import org.gangel.orders.proto.PingRequest;
import org.gangel.orders.proto.PingResponse;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@GRpcService
@Component
public class OrdersGrpcService extends OrdersServiceImplBase  {

    @Value("${grpc.port}")
    private int instancePort;    
    
    @Autowired
    private OrdersDataService ordersDataService;
    
    @Override
    public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
        responseObserver.onNext(PingResponse.newBuilder().setResponse(Integer.toString(instancePort)).build());
        responseObserver.onCompleted();
    }

    @Override
    public void addNewOrder(NewOrderRequest request,
            StreamObserver<NewOrderResponse> responseObserver) {
        Long id = ordersDataService.save(request.getOrders());
        responseObserver.onNext(NewOrderResponse.newBuilder().setId(id).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getOrder(GetOrderRequest request,
            StreamObserver<GetOrderResponse> responseObserver) {
        Orders orders = ordersDataService.getById(request.getId());
        responseObserver.onNext(GetOrderResponse.newBuilder().setOrders(orders).build());
        responseObserver.onCompleted();
    }

}
