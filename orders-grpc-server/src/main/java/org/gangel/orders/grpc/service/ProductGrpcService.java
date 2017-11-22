package org.gangel.orders.grpc.service;

import io.grpc.stub.StreamObserver;
import org.gangel.orders.grpc.service.data.ProductDataService;
import org.gangel.orders.proto.GetProductRequest;
import org.gangel.orders.proto.GetProductResponse;
import org.gangel.orders.proto.NewProductRequest;
import org.gangel.orders.proto.NewProductResponse;
import org.gangel.orders.proto.Product;
import org.gangel.orders.proto.ProductServiceGrpc.ProductServiceImplBase;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@GRpcService
@Component
public class ProductGrpcService extends ProductServiceImplBase  {

    @Autowired
    private ProductDataService productDataService;
    
    @Override
    public void createNewProduct(NewProductRequest request,
            StreamObserver<NewProductResponse> responseObserver) {
        Long id = productDataService.save(request.getProduct());
        responseObserver.onNext(NewProductResponse.newBuilder().setId(id).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getProduct(GetProductRequest request,
            StreamObserver<GetProductResponse> responseObserver) {
        Product product = productDataService.getById(request.getId());
        responseObserver.onNext(GetProductResponse.newBuilder().setProduct(product).build());
        responseObserver.onCompleted();
    }

}
