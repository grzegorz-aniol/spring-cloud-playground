package org.gangel.orders.grpc.executors;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.GeneratedMessageV3;
import io.grpc.ManagedChannel;
import io.netty.util.internal.ThreadLocalRandom;
import org.gangel.jperfstat.Histogram;
import org.gangel.orders.job.Configuration;
import org.gangel.orders.proto.GetProductRequest;
import org.gangel.orders.proto.NewProductRequest;
import org.gangel.orders.proto.Product;
import org.gangel.orders.proto.ProductServiceGrpc;
import org.gangel.orders.proto.ProductServiceGrpc.ProductServiceFutureStub;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class ProductServiceExecutor extends AbstractGrpcServiceExecutor<ProductServiceFutureStub> {

    public ProductServiceExecutor(Function<ProductServiceFutureStub, ListenableFuture<? extends GeneratedMessageV3>> requestFunction) {
        super(requestFunction);
    }

    @Override
    protected ProductServiceFutureStub produceStub(ManagedChannel channel) {
        return ProductServiceGrpc.newFutureStub(channel);
    }
    
    public static ProductServiceExecutor getNewProductRequestExecutor() {
        return new ProductServiceExecutor((stub) -> {
            return stub.createNewProduct(NewProductRequest.newBuilder()
                    .setProduct(Product.newBuilder()
                            .setTitle(UUID.randomUUID().toString().substring(0, 10))
                            .setDescription(UUID.randomUUID().toString().substring(0,10))
                            .setPrice(ThreadLocalRandom.current().nextDouble(0.01, 1000.0))
                            .build())
                    .build());
        });
    }

    public static Callable<Histogram> getGetProductRequestExecutor() {
        return new ProductServiceExecutor((stub) -> {
            return stub.getProduct(GetProductRequest.newBuilder()
                    .setId(ThreadLocalRandom.current()
                            .nextInt(Configuration.minProductId, Configuration.maxProductId))
                    .build());
        });
    }

}
