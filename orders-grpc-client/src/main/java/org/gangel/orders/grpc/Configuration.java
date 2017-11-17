package org.gangel.orders.grpc;

public class Configuration {

    public static long numOfIterations = 50_000;
    public static int numOfThreads = 1;
    public static String certFilePath;
    public static String host = "localhost";
    public static int port = 6565;
    public static String jobName = "ping";
    
    public static JobType jobType = JobType.UNKNOWN;
    

}
