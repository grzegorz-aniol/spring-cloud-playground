package org.gangel.jperfstat;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

/**
 * Print garbage collector statistics
 *  
 * @author Grzegorz_Aniol
 *
 */
public class GCStats {

    public static void printGCStats() {
        long totalGarbageCollections = 0;
        long garbageCollectionTime = 0;

        for(GarbageCollectorMXBean gc :
                ManagementFactory.getGarbageCollectorMXBeans()) {

            long count = gc.getCollectionCount();

            if(count >= 0) {
                totalGarbageCollections += count;
            }

            long time = gc.getCollectionTime();

            if(time >= 0) {
                garbageCollectionTime += time;
            }
        }

        System.out.print(String.format("Total # of GC: %d;", totalGarbageCollections));
        System.out.print(String.format("Total GC Time (ms): %d;", garbageCollectionTime));
    }    
    
}
