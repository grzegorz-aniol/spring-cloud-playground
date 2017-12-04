package org.gangel.orders.job;

import org.gangel.jperfstat.Histogram;
import org.gangel.jperfstat.HistogramStatsFormatter;
import org.gangel.jperfstat.ResultsTable;
import org.gangel.jperfstat.ResultsTable.RowBuilder;

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
        long totalRequestsCount = (Configuration.numOfIterations * Configuration.numOfThreads);
        
        ResultsTable resultsTable = HistogramStatsFormatter.createStatsResultTable();
        RowBuilder rowBuilder = HistogramStatsFormatter.addStatsRow(resultsTable, jobType.toString(), stats);
        rowBuilder.set("IOPS", totalRequestsCount / (1e-3 * stats.getExecutionTime().toMillis()));
        rowBuilder.set("Total Job Time[s]", 1e-3 * executionTime);
        rowBuilder.set("Threads", Configuration.numOfThreads);
        rowBuilder.set("Iterations/sec", Configuration.numOfIterations);
        rowBuilder.set("Total requests", totalRequestsCount);
        rowBuilder.build();
        
        resultsTable.outputAsCsv();
    }

}
