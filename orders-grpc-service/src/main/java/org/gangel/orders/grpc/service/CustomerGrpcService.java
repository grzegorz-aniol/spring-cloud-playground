package org.gangel.orders.grpc.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.gangel.orders.grpc.service.data.CustomerDataService;
import org.gangel.orders.grpc.service.data.OrdersDataService;
import org.gangel.orders.proto.Customer;
import org.gangel.orders.proto.CustomerServiceGrpc.CustomerServiceImplBase;
import org.gangel.orders.proto.GetCustomerOrdersRequest;
import org.gangel.orders.proto.GetCustomerOrdersResponse;
import org.gangel.orders.proto.GetCustomerRequest;
import org.gangel.orders.proto.GetCustomerResponse;
import org.gangel.orders.proto.NewCustomerRequest;
import org.gangel.orders.proto.NewCustomerResponse;
import org.gangel.orders.proto.SetOfIds;
import org.gangel.orders.repository.IdsRange;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@GRpcService
@Component
public class CustomerGrpcService extends CustomerServiceImplBase  {
    
    @Autowired
    private CustomerDataService customerDataService;
    
    @Autowired
    private OrdersDataService ordersDataService;
    
    @Override
    public void createNewCustomer(NewCustomerRequest request,
            StreamObserver<NewCustomerResponse> responseObserver) {
        
        Long id = customerDataService.save(request.getCustomer());
        
        responseObserver.onNext(NewCustomerResponse.newBuilder().setId(id).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getCustomer(GetCustomerRequest request,
            StreamObserver<GetCustomerResponse> responseObserver) {
        
        Customer customer = customerDataService.getById(request.getId());
        
        responseObserver.onNext(GetCustomerResponse.newBuilder().setCustomer(customer).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getCustomerIds(Empty request, StreamObserver<SetOfIds> responseObserver) {
        IdsRange idsRange = customerDataService.getIdsRange();
        responseObserver.onNext(SetOfIds.newBuilder()
                .addIds(idsRange.minId == null ? 0 : idsRange.minId)
                .addIds(idsRange.maxId == null ? 0 : idsRange.maxId)
                .build());
        responseObserver.onCompleted();
    }

    // server stream
    @Override
    public void getCustomerOrders(GetCustomerOrdersRequest request,
            StreamObserver<GetCustomerOrdersResponse> responseObserver) {
        
        ordersDataService.getCustomerOrders(request.getCustomerId())
            .forEach(it -> responseObserver.onNext(
                    GetCustomerOrdersResponse.newBuilder()
                        .setOrders(it)
                        .build())
            );
        
        responseObserver.onCompleted();
    }

    // client stream
    @Override
    public StreamObserver<NewCustomerRequest> updateCustomers(
            StreamObserver<com.google.protobuf.Empty> responseObserver) {
        return new StreamObserver<NewCustomerRequest>() {

            @Override
            public void onNext(NewCustomerRequest value) {
                customerDataService.save(value.getCustomer());
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(Empty.newBuilder().build());
                responseObserver.onCompleted();
            }
            
        };
    }

    //bidirectional stream
    @Override
    public StreamObserver<NewCustomerRequest> createNewCustomers(
            StreamObserver<NewCustomerResponse> responseObserver) {
        
        return new StreamObserver<NewCustomerRequest>() {

            @Override
            public void onNext(NewCustomerRequest value) {
                long id = customerDataService.save(value.getCustomer());
                responseObserver.onNext(NewCustomerResponse.newBuilder().setId(id).build());
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
            
        };
    }
    
}
