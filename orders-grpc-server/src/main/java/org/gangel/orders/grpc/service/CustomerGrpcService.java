package org.gangel.orders.grpc.service;

import io.grpc.stub.StreamObserver;
import org.gangel.orders.grpc.mappers.CustomerMapper;
import org.gangel.orders.proto.Customer;
import org.gangel.orders.proto.CustomerServiceGrpc.CustomerServiceImplBase;
import org.gangel.orders.proto.GetCustomerRequest;
import org.gangel.orders.proto.GetCustomerResponse;
import org.gangel.orders.proto.NewCustomerRequest;
import org.gangel.orders.proto.NewCustomerResponse;
import org.gangel.orders.repository.CustomerRepository;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@GRpcService
@Component
public class CustomerGrpcService extends CustomerServiceImplBase  {
    
    @Autowired
    private CustomerMapper customerMapper;
    
    @Autowired
    private CustomerRepository customerRepo;

    @Override
    public void createNewCustomer(NewCustomerRequest request,
            StreamObserver<NewCustomerResponse> responseObserver) {
        
        org.gangel.orders.entity.Customer customerEntity = customerMapper.toEntity(request.getCustomer());
        customerEntity.setId(null);
        customerEntity = customerRepo.save(customerEntity);
        
        responseObserver.onNext(NewCustomerResponse.newBuilder().setId(customerEntity.getId()).build());
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
