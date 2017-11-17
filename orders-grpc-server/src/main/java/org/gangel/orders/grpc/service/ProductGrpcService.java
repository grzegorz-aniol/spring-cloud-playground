package org.gangel.orders.grpc.service;

import io.grpc.stub.StreamObserver;
import org.gangel.orders.proto.GetProductRequest;
import org.gangel.orders.proto.GetProductResponse;
import org.gangel.orders.proto.NewProductRequest;
import org.gangel.orders.proto.NewProductResponse;
import org.gangel.orders.proto.ProductServiceGrpc.ProductServiceImplBase;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.stereotype.Component;

@GRpcService
@Component
public class ProductGrpcService extends ProductServiceImplBase  {

    @Override
    public void createNewProduct(NewProductRequest request,
            StreamObserver<NewProductResponse> responseObserver) {
        super.createNewProduct(request, responseObserver);
    }

    @Override
    public void getProduct(GetProductRequest request,
            StreamObserver<GetProductResponse> responseObserver) {
        super.getProduct(request, responseObserver);
    }

}
