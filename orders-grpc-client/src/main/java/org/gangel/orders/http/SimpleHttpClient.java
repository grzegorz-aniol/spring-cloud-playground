package org.gangel.orders.http;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleHttpClient {

    private static int numOfIterations = 1000;
    private static int numOfThreads = 1;
    private static String host = "localhost";
    private static int port = 8001;

    private static class Task implements Callable<Long> {

        public Task() {}

        @Override
        public Long call() throws Exception {

            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(numOfIterations);
            cm.setDefaultMaxPerRoute(numOfIterations);
            HttpHost localhost = new HttpHost(host, port);
            cm.setMaxPerRoute(new HttpRoute(localhost), numOfIterations);

            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
            HttpGet getRequest = new HttpGet("http://" + host + ":" + port +"/");

            long totalTime = 0;

            for (long i = 0; i < numOfIterations; ++i) {
                long t0 = System.currentTimeMillis();
                CloseableHttpResponse response = httpClient.execute(getRequest);
                totalTime += System.currentTimeMillis() - t0;
                
                int code = response.getStatusLine().getStatusCode();
                if (code != HttpStatus.SC_OK) {
                    throw new RuntimeException("Error code: " + code);
                }
            }

            return totalTime;

        }

    };

    public static void main(String[] args) {
        System.out.println("Starting " + SimpleHttpClient.class.getSimpleName());
        
        try {
            setOptions(args);
        } catch (Exception e2) {
            System.err.println(e2.getMessage());
            return; 
        }        
        
        System.out.println("Starting " + numOfThreads + " threads...");

        System.out.println("Waiting for temination...");

        ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
        List<Future<Long>> futures = null;
        long t0 = System.currentTimeMillis();
        try {
            futures = executor.invokeAll(
                    Stream.generate(() -> new Task()).limit(numOfThreads).collect(Collectors.toList()),
                    300, TimeUnit.SECONDS);
        } catch (InterruptedException e1) {
            System.err.println(e1.getMessage());
            return;
        }

        executor.shutdown();
        long executionTime = System.currentTimeMillis() - t0;

        long totalDuraionTime = 0;
        long totalRequestsCount = (numOfIterations * numOfThreads);

        for (int i = 0; i < futures.size(); ++i) {
            try {
                totalDuraionTime += futures.get(i).get();
            } catch (InterruptedException | ExecutionException e) {
                System.err.println(e.getMessage());
                return;
            }
        }

        System.out.println(String.format("Total requests sent = %d", totalRequestsCount));
        System.out.println(
                String.format("Execution time = %.3f sec; IOPS = %.0f; avg response = %.2f ms",
                        1e-3 * executionTime, totalRequestsCount / (1e-3 * executionTime),
                        (double) totalDuraionTime / totalRequestsCount));

        System.out.println(String.format("Done."));

    }

    private static void setOptions(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("i", "iterations", true, "Number of iterations");
        options.addOption("t", "threads", true, "Number of threads");
        options.addOption("h", "host", true, "Server address");
        options.addOption("p", "port", true, "Port");
        
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        
        int value = Integer.parseInt(cmd.getOptionValue("t"));
        if (value < 1) {            
            numOfThreads = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);
        } else {
            numOfThreads = value; 
        }
        
        String iterationValue = cmd.getOptionValue("i");
        if (iterationValue != null) {
            numOfIterations = Math.max(1, Integer.parseUnsignedInt(iterationValue));
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
}
