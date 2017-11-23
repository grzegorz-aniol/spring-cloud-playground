package org.gangel.orders.grpc.executors;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.GeneratedMessageV3;
import io.grpc.ManagedChannel;
import io.netty.util.internal.ThreadLocalRandom;
import org.gangel.jperfstat.Histogram;
import org.gangel.orders.job.Configuration;
import org.gangel.orders.proto.GetOrderRequest;
import org.gangel.orders.proto.NewOrderRequest;
import org.gangel.orders.proto.OrderItem;
import org.gangel.orders.proto.Orders;
import org.gangel.orders.proto.OrdersServiceGrpc;
import org.gangel.orders.proto.OrdersServiceGrpc.OrdersServiceFutureStub;
import org.gangel.orders.proto.PingRequest;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class OrdersServiceExecutor extends AbstractGrpcServiceExecutor<OrdersServiceFutureStub> {

    public OrdersServiceExecutor(Function<OrdersServiceFutureStub, ListenableFuture<? extends GeneratedMessageV3>> requestFunction) {
        super(requestFunction);
    }

    @Override
    protected OrdersServiceFutureStub produceStub(ManagedChannel channel) {
        return OrdersServiceGrpc.newFutureStub(channel);
    }
    
    public static OrdersServiceExecutor getPingExecutor() {
        return new OrdersServiceExecutor((stub) -> {
            return stub.ping(PingRequest.newBuilder().build());
        });
    }
    
    public static OrdersServiceExecutor getNewOrdersRequestExecutor() {
        return new OrdersServiceExecutor((stub) -> {
            
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            
            Orders.Builder ob = Orders.newBuilder()
                    .setCustomerId(rnd.nextInt(Configuration.minCustomerId, Configuration.maxCustomerId));
            
            for (int i=0; i < 4; ++i) {
                ob.addOrderItem(OrderItem.newBuilder()
                        .setLineNumber(i+1)
                        .setQuantity(rnd.nextInt(1,17))
                        .setAmount(rnd.nextInt(1,1500))
                        .setProductId(rnd.nextInt(Configuration.minProductId,Configuration.maxProductId))
                        .build()
                );
            }
            
            return stub.addNewOrder(NewOrderRequest.newBuilder()
                    .setOrders(ob.build())
                    .build());
        });
    }

    public static Callable<Histogram> getGetOrdersRequestExecutor() {
        return new OrdersServiceExecutor((stub) -> {
            return stub.getOrder(GetOrderRequest.newBuilder()
                    .setId(ThreadLocalRandom.current()
                            .nextInt(Configuration.minOrdersId, Configuration.maxOrdersId))
                    .build());
        });
    }

}
