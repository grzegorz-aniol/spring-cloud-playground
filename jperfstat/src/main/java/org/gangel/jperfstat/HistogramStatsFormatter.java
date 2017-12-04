package org.gangel.jperfstat;

public class HistogramStatsFormatter {

    public static ResultsTable createStatsResultTable() {
        return ResultsTable.withHeaders("Name", "Percentiles [ms]", "0th", "50th", "99th", "99.9th", "99.99th", "99.999th", "100th", "execution time[s]");
    }
    
    public static ResultsTable.RowBuilder addStatsRow(final ResultsTable table, final String name, Histogram.Statistics stats) {
        return table.withRow(name)
            .add(" ") // Precentiles [ms] 
            .add(Histogram.Statistics.convertToMils(stats.p0th)) 
            .add(Histogram.Statistics.convertToMils(stats.p50th)) 
            .add(Histogram.Statistics.convertToMils(stats.p99th)) 
            .add(Histogram.Statistics.convertToMils(stats.p99d9th)) 
            .add(Histogram.Statistics.convertToMils(stats.p99d99th)) 
            .add(Histogram.Statistics.convertToMils(stats.p99d999th)) 
            .add(Histogram.Statistics.convertToMils(stats.p100th)) 
            .add(stats.startTime != null && stats.stopTime != null ? 1e-3*(stats.stopTime.toMillis() - stats.startTime.toMillis()) : Double.NaN);
    };
 
}
