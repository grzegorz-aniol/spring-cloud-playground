package org.gangel.orders.grpc;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.gangel.orders.grpc.common.GlobalExceptionHandler;

public class OrdersGRpcClientApp {

    public static void setOptions(String[] args) throws Exception {
        Options options = new Options();
        options.addOption("cert",true, "Path to server certificate");
        options.addOption("i", "iterations", true, "Number of iterations");
        options.addOption("t", "threads", true, "Number of threads");
        options.addOption("h", "host", true, "Server address");
        options.addOption("p", "port", true, "Port");
        options.addOption("j", "job", true, "Job name to do (ping, newcustomer)");
        
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        Configuration.certFilePath = cmd.getOptionValue("cert");
        if (Configuration.certFilePath == null) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("GRpcClient", options);            
            throw new Exception("Certificate path not found. Please specify parameter!");            
        }
        
        String iterationValue = cmd.getOptionValue("i");
        if (iterationValue != null) {
            Configuration.numOfIterations = Math.max(1, Integer.parseUnsignedInt(iterationValue));
        }        
        
        Configuration.numOfThreads = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);
        String numOfThreadsTemp = cmd.getOptionValue("t");
        if (numOfThreadsTemp != null) {
            int value = Integer.parseInt(numOfThreadsTemp);
            if (value > 0) {            
                Configuration.numOfThreads = Math.max(1, value); 
            }            
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
    }
    
    public static void main(String[] args) {
        GlobalExceptionHandler.register();
        
        System.out.println("Launching " + OrdersGRpcClientApp.class.getSimpleName());        
        
        try {
            setOptions(args);
        } catch (Exception e2) {
            System.err.println(e2.getMessage());
            e2.printStackTrace();
            return; 
        }
        
        JobManager mgr = JobManager.getJobManagerForJobType(Configuration.jobType);
        
        mgr.run();
        
    }


}
