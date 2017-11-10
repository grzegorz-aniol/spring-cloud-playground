package org.gangel.orders.grpc;

public class Configuration {

    static long numOfIterations = 50_000;
    static int numOfThreads = 1;
    static String certFilePath;
    static String host = "localhost";
    static int port = 6565;
    static String jobName = "ping";
    
    public static JobType jobType = JobType.UNKNOWN;
    

}
