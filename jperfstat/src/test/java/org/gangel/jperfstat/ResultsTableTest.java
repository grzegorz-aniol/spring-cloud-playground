package org.gangel.jperfstat;

import org.junit.Test;

public class ResultsTableTest {

    @Test
    public void testSetMethod() {
        ResultsTable table = new ResultsTable()
            .withHeaders("operation", "50th", "99th", "99.9th", "IOPS");
        table.withRow("PING")
            .set("operation", "PING")
            .set("50th", "123")
            .set("99th", "45");
        
        table.outputAsCsv();
        table.outputAsData();
    }
    
    @Test
    public void testAddBuilder() {
        ResultsTable table = new ResultsTable()
            .withHeaders("operation", "50th", "99th", "99.9th", "IOPS");
        table.withRow("PING")
            .add("123")
            .add("335")
            .add("456")
            .add("1024")
            .build();
            
        table.outputAsCsv();
        table.outputAsData();
    }
    
    @Test
    public void testMoveRows() {
        ResultsTable table = new ResultsTable()
            .withHeaders("operation", "50th", "99th", "99.9th", "IOPS");
        
        table.withRow("PING").add("123").add("335").add("456").add("1024").build();
        table.withRow("HTTPS 1.1").add("123").add("335").add("456").add("1024").build();
        table.withRow("HTTPS 2.0").add("56").add("67").add("78").add("89").build();
        
        table.outputAsCsv();
        table.outputAsData();
    }    
    
    @Test
    public void testDouleRows() {
        ResultsTable table = new ResultsTable()
            .withHeaders("operation", "50th", "99th", "99.9th", "IOPS");
        
        table.withRow("PING").withPrecision(2).add(12.3).add(33.5).add(45.6).add(1024.4567).build();
        table.withRow("HTTPS 1.1").withPrecision(3).add(12.3).add(33.5).add(45.6).add(1024.4567).build();
        table.withRow("HTTPS 2.0").withPrecision(4).add(12.3).add(33.5).add(45.6).add(1024.4567).build();
        
        table.outputAsCsv();
        table.outputAsData();
    }    
}
