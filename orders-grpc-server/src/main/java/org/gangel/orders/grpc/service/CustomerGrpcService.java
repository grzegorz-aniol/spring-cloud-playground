package org.gangel.orders.grpc.service;

import io.grpc.stub.StreamObserver;
import org.gangel.orders.grpc.service.data.CustomerDataService;
import org.gangel.orders.proto.Customer;
import org.gangel.orders.proto.CustomerServiceGrpc.CustomerServiceImplBase;
import org.gangel.orders.proto.GetCustomerRequest;
import org.gangel.orders.proto.GetCustomerResponse;
import org.gangel.orders.proto.NewCustomerRequest;
import org.gangel.orders.proto.NewCustomerResponse;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@GRpcService
@Component
public class CustomerGrpcService extends CustomerServiceImplBase  {
    
    @Autowired
    private CustomerDataService customerDataService;
    
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
    
}
