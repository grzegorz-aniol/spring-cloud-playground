package org.gangel.orders.job;

import org.gangel.jperfstat.GCStats;
import org.gangel.jperfstat.Histogram;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JobManager implements Runnable {

    private Supplier<? extends Callable<Histogram>> requestTaskSupplier;
    private JobType jobType;

    public JobManager(JobType jobType, Supplier<? extends Callable<Histogram>> requestTaskSupplier) {
        this.jobType = jobType;
        this.requestTaskSupplier = requestTaskSupplier;
    }

    @Override
    public void run() {
        System.out.println(String.format("Starting job '%s' with %d threads", jobType.getName(), Configuration.numOfThreads));        
        System.out.println("Waiting for termination...");
        
        ExecutorService executor = Executors.newFixedThreadPool(Configuration.numOfThreads);
        List<Future<Histogram>> futures = null;
        long t0 = System.currentTimeMillis();
        try {
            futures = executor.invokeAll(
                    Stream.generate(requestTaskSupplier)
                        .limit(Configuration.numOfThreads)
                        .collect(Collectors.toList()), 
                    300, TimeUnit.SECONDS);
        } catch (InterruptedException e1) {
            System.err.println(e1.getMessage());
            e1.printStackTrace();
            return; 
        }
        
        executor.shutdown();
        long executionTime = System.currentTimeMillis() - t0;        
        
        List<Histogram> histograms;
        histograms = futures.stream().map(v -> {
            try {
                return v.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        
        Histogram.Statistics stats = Histogram.getStats(histograms);
        long totalDuraionTime = stats.totalTime.toMillis();
        long totalRequestsCount = (Configuration.numOfIterations * Configuration.numOfThreads);
        
        System.out.print(String.format("Job = %s;", jobType.toString()));
        System.out.print(stats.toString());
        System.out.print(String.format("Execution time = %.3f sec; IOPS = %.0f; avg response = %.2f ms;", 
                1e-3*executionTime, totalRequestsCount/(1e-3*executionTime), (double)totalDuraionTime/totalRequestsCount));
        System.out.print(String.format("Threads = %d;", Configuration.numOfThreads));
        System.out.print(String.format("Iterations per thread = %d;", Configuration.numOfIterations));
        System.out.print(String.format("Total requests sent = %d;", totalRequestsCount));
        GCStats.printGCStats();
        System.out.println();
        System.out.println(String.format("Done."));
    }

}
