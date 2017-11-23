package org.gangel.orders.grpc.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.time.temporal.ChronoUnit;

public class HistogramUnitTests {

    @Test
    public void convertNanoToSec() {
        assertEquals(123, Histogram.convert(123_000_000_000L, ChronoUnit.NANOS, ChronoUnit.SECONDS));
    }

    @Test
    public void convertSecToNano() {
        assertEquals(3_000_000_000L, Histogram.convert(3L, ChronoUnit.SECONDS, ChronoUnit.NANOS));
    }
    
    @Test
    public void convertMillsToNano() {
        assertEquals(3_000_000L, Histogram.convert(3L, ChronoUnit.MILLIS, ChronoUnit.NANOS));
    }

    @Test
    public void convertNanoToMills() {
        assertEquals(1L, Histogram.convert(1_000_000, ChronoUnit.NANOS, ChronoUnit.MILLIS));
    }
    
}
