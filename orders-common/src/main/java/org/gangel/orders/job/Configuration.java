package org.gangel.orders.job;

public class Configuration {

    public static int numOfIterations = 50_000;
    public static int numOfWarmIterations = 0;    
    public static int numOfThreads = 1;
    public static String certFilePath;
    public static String host = "localhost";
    public static int port = 6565;
    public static String jobName = "ping";
    
    public static JobType jobType = JobType.UNKNOWN;
    
    public static int minCustomerId = 1;
    public static int maxCustomerId = 10_000;
    
    public static int minProductId = 1;
    public static int maxProductId = 10_000;
    
    public static int minOrdersId = 1;
    public static int maxOrdersId = 10_000;
}
