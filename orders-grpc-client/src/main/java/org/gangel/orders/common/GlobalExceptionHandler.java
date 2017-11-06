package org.gangel.orders.common;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println(e.getMessage());
        e.printStackTrace(System.out);
    }
    
    public static void register() {
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
    }
    
}
