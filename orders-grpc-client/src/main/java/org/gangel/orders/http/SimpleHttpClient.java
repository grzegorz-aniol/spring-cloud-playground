package org.gangel.orders.http;

import lombok.Getter;
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
import org.gangel.orders.grpc.Configuration;
import org.gangel.orders.grpc.common.Histogram;
import org.gangel.orders.grpc.common.Histogram.Statistics;

import java.io.ByteArrayOutputStream;
import java.time.temporal.ChronoUnit;
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

    private static class Task implements Callable<Histogram> {

        public Task() {}
        
        @Getter
        private Histogram histogram; 

        @Override
        public Histogram call() throws Exception {

            histogram = new Histogram(numOfIterations, ChronoUnit.NANOS);
            
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(numOfIterations);
            cm.setDefaultMaxPerRoute(numOfIterations);
            HttpHost localhost = new HttpHost(host, port);
            cm.setMaxPerRoute(new HttpRoute(localhost), numOfIterations);

            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

            for (long i = 0; i < numOfIterations; ++i) {
                HttpGet getRequest = new HttpGet("http://" + host + ":" + port +"/");
                //getRequest.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
//                getRequest.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);

                long t0 = System.nanoTime();
                CloseableHttpResponse response = httpClient.execute(getRequest);
                
                int code = response.getStatusLine().getStatusCode();
                if (code != HttpStatus.SC_OK) {
                    throw new RuntimeException("Error code: " + code);
                }
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                response.getEntity().writeTo(output);
                response.close();
                
                long time = System.nanoTime() - t0;
                histogram.put(time);
            }

            httpClient.close();
            
            return histogram;

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

        System.out.println("Waiting for termination...");

        ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
        List<Future<Histogram>> futures = null;
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

        List<Histogram> histograms = futures.stream().map(v->{
            try {
                return v.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        
        long totalRequestsCount = (numOfIterations * numOfThreads);
        
        Statistics stats = Histogram.getStats(histograms);
        System.out.print(stats.toString());
        long totalDuraionTime = stats.totalTime.toMillis();
        System.out.print(String.format("Total requests sent = %d;", totalRequestsCount));
        System.out.print(
                String.format("Execution time = %.3f sec; IOPS = %.0f; avg response = %.2f ms;",
                        1e-3 * executionTime, totalRequestsCount / (1e-3 * executionTime),
                        (double) totalDuraionTime / totalRequestsCount));
        System.out.print(String.format("Threads = %d;", Configuration.numOfThreads));
        System.out.print(String.format("Iterations per thread = %d;", Configuration.numOfIterations));
        
        System.out.println(String.format("\nDone."));

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
