package org.gangel.orders.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.gangel.orders.common.GlobalExceptionHandler;
import org.gangel.orders.proto.Customer;
import org.gangel.orders.proto.NewCustomerRequest;
import org.gangel.orders.proto.NewCustomerResponse;
import org.gangel.orders.proto.OrdersServiceGrpc;
import org.gangel.orders.proto.OrdersServiceGrpc.OrdersServiceFutureStub;

import java.io.File;
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

    private static long numOfIterations = 50_000;
    private static int numOfThreads = 1; 
    private static String certFilePath; 
    private static String host = "localhost";
    private static int port = 6565;
    
    private static class Task implements Callable<Long> {

        public Task() {
                        
        }
        
        @Override
        public Long call() throws Exception {
            GlobalExceptionHandler.register();

            final ManagedChannel channel = NettyChannelBuilder.forAddress(host, port)
                        .sslContext(GrpcSslContexts.forClient()
                                .trustManager(new File(certFilePath))
                                .build())
                        .build();
                    
            final OrdersServiceFutureStub stub = OrdersServiceGrpc.newFutureStub(channel);
            
            long totalTime = 0;
            
            for (long i=0; i < numOfIterations; ++i) {
                NewCustomerRequest request = NewCustomerRequest.newBuilder()
                        .setCustomer(Customer.newBuilder()
                                .setId(1)
                                .build())
                        .build();            
                try {
                    ListenableFuture<NewCustomerResponse> future = stub.createNewCustomer(request);
                    long t0 = System.currentTimeMillis();
                    NewCustomerResponse response = future.get(5, TimeUnit.SECONDS);
                    totalTime += (System.currentTimeMillis() - t0);
                    //System.out.println("Object ID = " + response.getId());
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    return 0L; 
                } catch (TimeoutException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    return 0L; 
                }
            }
            
            channel.shutdown();
            channel.awaitTermination(5, TimeUnit.SECONDS);
            
            return totalTime;
        }
        
    };

    public static void setOptions(String[] args) throws Exception {
        Options options = new Options();
        options.addOption("cert",true, "Path to server certificate");
        options.addOption("i", "iterations", true, "Number of iterations");
        options.addOption("t", "threads", true, "Number of threads");
        options.addOption("h", "host", true, "Server address");
        options.addOption("p", "port", true, "Port");
        
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        certFilePath = cmd.getOptionValue("cert");
        if (certFilePath == null) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("GRpcClient", options);            
            throw new Exception("Certificate path not found. Please specify parameter!");            
        }
        
        String iterationValue = cmd.getOptionValue("i");
        if (iterationValue != null) {
            numOfIterations = Math.max(1, Integer.parseUnsignedInt(iterationValue));
        }        
        
        numOfThreads = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);
        String numOfThreadsTemp = cmd.getOptionValue("t");
        if (numOfThreadsTemp != null) {
            int value = Integer.parseInt(numOfThreadsTemp);
            if (value > 0) {            
                numOfThreads = Math.max(1, value); 
            }            
        }
        
        String hostValue = cmd.getOptionValue("h");
        if (hostValue!=null) {
            host = hostValue;
        }
        
        String portValue = cmd.getOptionValue("p");
        if (portValue != null) {
            port = Math.max(1, Integer.parseUnsignedInt(portValue)); 
        }        
    }
    
    public static void main(String[] args) {
        GlobalExceptionHandler.register();
        
        System.out.println("Starting " + OrdersGRpcClientApp.class.getSimpleName());        
        
        try {
            setOptions(args);
        } catch (Exception e2) {
            System.err.println(e2.getMessage());
            e2.printStackTrace();
            return; 
        }
        
        System.out.println("Starting " + numOfThreads + " threads...");
        
        System.out.println("Waiting for termination...");
        
        ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
        List<Future<Long>> futures = null;
        long t0 = System.currentTimeMillis();
        try {
            futures = executor.invokeAll(
                    Stream.generate(()->new Task()).limit(numOfThreads).collect(Collectors.toList()), 
                    300, TimeUnit.SECONDS);
        } catch (InterruptedException e1) {
            System.err.println(e1.getMessage());
            e1.printStackTrace();
            return; 
        }
        
        executor.shutdown();
        long executionTime = System.currentTimeMillis() - t0;
        
        long totalDuraionTime = 0;
        long totalRequestsCount = (numOfIterations * numOfThreads);
        
        for (int i=0; i < futures.size(); ++i) {
            try {
                totalDuraionTime += futures.get(i).get();
            } catch (InterruptedException | ExecutionException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                return;
            }
        }

        System.out.print(String.format("Threads = %d;", numOfThreads));
        System.out.print(String.format("Iterations per thread = %d;", numOfIterations));
        System.out.print(String.format("Total requests sent = %d;", totalRequestsCount));
        System.out.print(String.format("Execution time = %.3f sec; IOPS = %.0f; avg response = %.2f ms", 
                1e-3*executionTime, totalRequestsCount/(1e-3*executionTime), (double)totalDuraionTime/totalRequestsCount));
        System.out.println();
        System.out.println(String.format("Done."));
    }
}
