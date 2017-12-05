package org.gangel.jperfstat;

public class HistogramStatsFormatter {

    public static ResultsTable.RowBuilder addStatsRow(final ResultsTable.RowBuilder rowBuilder, Histogram.Statistics stats) {
        return rowBuilder
            .set("Percentiles [ms]", " ")
            .set("0th",  Histogram.Statistics.convertToMils(stats.p0th)) // best time
            .set("50th", Histogram.Statistics.convertToMils(stats.p50th)) 
            .set("99th", Histogram.Statistics.convertToMils(stats.p99th)) 
            .set("99.9th", Histogram.Statistics.convertToMils(stats.p99d9th)) 
            .set("99.99th", Histogram.Statistics.convertToMils(stats.p99d99th)) 
            .set("99.999th",Histogram.Statistics.convertToMils(stats.p99d999th)) 
            .set("100th", Histogram.Statistics.convertToMils(stats.p100th)) // worst time
            .set("execution time[s]", stats.startTime != null && stats.stopTime != null ? 1e-3*(stats.stopTime.toMillis() - stats.startTime.toMillis()) : Double.NaN);
    };
 
}
