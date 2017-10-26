package org.gangel.orders.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.gangel.orders.proto.Customer;
import org.gangel.orders.proto.NewCustomerRequest;
import org.gangel.orders.proto.NewCustomerResponse;
import org.gangel.orders.proto.OrdersServiceGrpc;
import org.gangel.orders.proto.OrdersServiceGrpc.OrdersServiceFutureStub;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrdersGRpcClientApp {

    private static final long COUNT = 10_000;
    private static int THREADS = 1; 
    
    private static class Task implements Callable<Long> {

        public Task() {
        }
        
        @Override
        public Long call() throws Exception {
            final ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                    .usePlaintext(true)
                    .build();
            final OrdersServiceFutureStub stub = OrdersServiceGrpc.newFutureStub(channel);
            
            long totalTime = 0;
            
            for (long i=0; i < COUNT; ++i) {
                NewCustomerRequest request = NewCustomerRequest.newBuilder()
                        .setCustomer(Customer.newBuilder()
                                .setId(1)
                                .build())
                        .build();            
                try {
                    ListenableFuture<NewCustomerResponse> future = stub.createNewCustomer(request);
                    long t0 = System.currentTimeMillis();
                    NewCustomerResponse response = future.get(1, TimeUnit.SECONDS);
                    totalTime += (System.currentTimeMillis() - t0);
                    //System.out.println("Object ID = " + response.getId());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            
            return totalTime;
        }
        
    };

    public static void main(String[] args) {
        
        THREADS = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);
        
        System.out.println("Starting " + THREADS + " threads...");
        
        System.out.println("Waiting for temination...");
        
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        List<Future<Long>> futures = null;
        long t0 = System.currentTimeMillis();
        try {
            futures = executor.invokeAll(
                    Stream.generate(()->new Task()).limit(THREADS).collect(Collectors.toList()), 
                    300, TimeUnit.SECONDS);
        } catch (InterruptedException e1) {
            System.err.println(e1.getMessage());
            return; 
        }
        
        executor.shutdown();
        long executionTime = System.currentTimeMillis() - t0;
        
        long totalDuraionTime = 0;
        long totalRequestsCount = (COUNT * THREADS);
        
        for (int i=0; i < futures.size(); ++i) {
            try {
                totalDuraionTime += futures.get(i).get();
            } catch (InterruptedException | ExecutionException e) {
                System.err.println(e.getMessage());
                return;
            }
        }

        System.out.println(String.format("Total requests sent = %d", totalRequestsCount));
        System.out.println(String.format("Execution time = %.3f sec; IOPS = %.0f; avg response = %.2f ms", 
                1e-3*executionTime, totalRequestsCount/(1e-3*executionTime), (double)totalDuraionTime/totalRequestsCount));
        
        System.out.println(String.format("Done."));
    }
}
