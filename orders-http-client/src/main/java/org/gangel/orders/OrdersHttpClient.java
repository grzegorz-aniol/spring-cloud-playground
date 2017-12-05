package org.gangel.orders;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.gangel.orders.job.Configuration;
import org.gangel.orders.job.JobManager;
import org.gangel.orders.job.JobType;

public class OrdersHttpClient {

    public static void main(String[] args) {
        GlobalExceptionHandler.register();
        System.out.println("Starting " + OrdersHttpClient.class.getSimpleName());
        
        try {
            setOptions(args);
        } catch (Exception e2) {
            System.err.println(e2.getMessage());
            return; 
        }        
        
        if (Configuration.isSSL) {
            System.out.println("SSL is enabled");
            System.setProperty("javax.net.ssl.trustStore", Configuration.sslCertFile);
        }
        
        JobManager job = Configuration.jobType.accept(new JobTypeVisitor());
        job.run();
        
        System.out.println("\nDone.");
    }

    private static void setOptions(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("i", "iterations", true, "Number of iterations");
        options.addOption("wi", "warm-iterations", true, "Number of warming iterations");
        options.addOption("t", "threads", true, "Number of threads");
        options.addOption("h", "host", true, "Server address");
        options.addOption("p", "port", true, "Port");
        options.addOption("j", "job", true, "Job name to do (ping, newcustomer)");
        options.addOption("", "ssl", false, "Enable SSL connection");
        options.addOption("", "cert", true, "SSL certificate file");
        
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        
        int value = Integer.parseInt(cmd.getOptionValue("t"));
        if (value < 1) {
            Configuration.numOfThreads = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);
        } else {
            Configuration.numOfThreads = value; 
        }
        
        String iterationValue = cmd.getOptionValue("i");
        if (iterationValue != null) {
            Configuration.numOfIterations = Math.max(1, Integer.parseUnsignedInt(iterationValue));
        }
        iterationValue = cmd.getOptionValue("wi");
        if (iterationValue != null) {
            Configuration.numOfWarmIterations = Math.max(0, Integer.parseUnsignedInt(iterationValue));
        }  
        
        String hostValue = cmd.getOptionValue("h");
        if (hostValue!=null) {
            Configuration.host = hostValue;
        }
        
        String portValue = cmd.getOptionValue("p");
        if (portValue != null) {
            Configuration.port = Math.max(1, Integer.parseUnsignedInt(portValue)); 
        }
        String jobNameValue = cmd.getOptionValue("j");
        if (jobNameValue != null) {
            Configuration.jobType = JobType.valueOf(jobNameValue);
        }        
        
        Configuration.isSSL = cmd.hasOption("ssl");
        
        String sslPath = cmd.getOptionValue("cert");
        if (sslPath!=null && !sslPath.isEmpty()) {
            Configuration.sslCertFile = sslPath; 
        }
        
        if (Configuration.isSSL && Configuration.sslCertFile==null) {
            throw new RuntimeException("SSL certification path is not specified!");
        }
        
        Configuration.appName = (Configuration.isSSL ? "Orders-HTTPS-Client" : "Orders-HTTP-Client");
    }
}
