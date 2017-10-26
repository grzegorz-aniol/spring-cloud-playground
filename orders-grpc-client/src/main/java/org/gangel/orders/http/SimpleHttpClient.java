package org.gangel.orders.http;

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

    private static final int COUNT = 1000;
    private static int THREADS = 1;

    private static class Task implements Callable<Long> {

        public Task() {}

        @Override
        public Long call() throws Exception {

            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(COUNT);
            cm.setDefaultMaxPerRoute(COUNT);
            HttpHost localhost = new HttpHost("locahost", 8001);
            cm.setMaxPerRoute(new HttpRoute(localhost), COUNT);

            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
            HttpGet getRequest = new HttpGet("http://localhost:8001/");

            long totalTime = 0;

            for (long i = 0; i < COUNT; ++i) {
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
        THREADS = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);

        System.out.println("Starting " + THREADS + " threads...");

        System.out.println("Waiting for temination...");

        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        List<Future<Long>> futures = null;
        long t0 = System.currentTimeMillis();
        try {
            futures = executor.invokeAll(
                    Stream.generate(() -> new Task()).limit(THREADS).collect(Collectors.toList()),
                    300, TimeUnit.SECONDS);
        } catch (InterruptedException e1) {
            System.err.println(e1.getMessage());
            return;
        }

        executor.shutdown();
        long executionTime = System.currentTimeMillis() - t0;

        long totalDuraionTime = 0;
        long totalRequestsCount = (COUNT * THREADS);

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
}
