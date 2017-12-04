package org.gangel.jperfstat;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class ResultsTable {

    public static class RowBuilder {
        private Iterator<String> columnsIterator;
        private Row row;
        private int precision = 3; 
        
        private RowBuilder(Row row, Iterator<String> columnsIterator) {
            this.row = row;
            this.columnsIterator = columnsIterator;
        }
        
        public RowBuilder withPrecision(int precision) {
            this.precision = precision;
            return this; 
        }
        
        public RowBuilder add(double value) {
            return this.add(String.format("%." + Integer.toString(precision) + "f", value));
        }
        
        public RowBuilder add(String value) {
            String column = value; 
            if (columnsIterator != null) {
                if (!columnsIterator.hasNext()) {
                    throw new RuntimeException(String.format("Can't add value '%s'. Missing column at this position.", value));
                }
                column = columnsIterator.next();                
            }
            row.values.put(column, value);
            return this; 
        }
        
        public RowBuilder set(String key, String value) {
            row.parentRef.header.values.computeIfAbsent(key, (k)->k);
            row.values.put(key, value);
            return this; 
        }
                
        public RowBuilder set(String key, double value) {
            set(key, String.format("%." + Integer.toString(precision) + "f", value));
            return this; 
        }
        
        public ResultsTable build() {
            return row.parentRef; 
        }
    }
    
    public static class Row {
        private Row header; 
        private String name; 
        private LinkedHashMap<String, String> values = new LinkedHashMap<>(); 
        private ResultsTable parentRef; 
        
        private Row(final ResultsTable parentRef, final Row header, final String rowName) {
            this.parentRef = parentRef;
            this.name = rowName;
            this.header = header;
        }

        private RowBuilder add(String value) {
            RowBuilder rb = new RowBuilder(this, header != null ? header.values.keySet().iterator() : null);
            rb.add(value);
            return rb; 
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Row other = (Row) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }
    };
    
    private Row header = new Row(this, null, "");
    private LinkedHashMap<String, Row> rows = new LinkedHashMap<>();

    public ResultsTable() {
    }

    public ResultsTable addHeaders(final String... names) {
        for (String s : names) {
            header.values.put(s, s);
        }
        return this;
    }
    
    public static ResultsTable withHeaders(final String... names) {
        ResultsTable result = new ResultsTable();
        for (String s : names) {
            result.header.values.put(s, s);
        }
        return result;
    }

    public RowBuilder withRow(final String rowName) {
        Row row = rows.compute(rowName, (k,v) -> v == null ? new Row(this, header, rowName) : v );
        return row.add(rowName);
    }
    
    private void outputCsvRow(StringBuffer sb, Row row) {
        Iterator<Entry<String, String>> iterator = row.values.entrySet().iterator();
        while(iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            sb.append(entry.getValue()).append(";");
        }
        sb.append("\n");
    }
    
    private void outputCsvDataRow(StringBuffer sb, Row row) {
        for(String header : header.values.values()) {
            String value = row.values.get(header);
            sb.append(value==null ? "" : value).append(";");
        }
        sb.append("\n");
    }
    
    public void outputAsCsv() {
        StringBuffer sb = new StringBuffer();
        outputCsvRow(sb, header); 
        for (Row r : rows.values()) {
            outputCsvDataRow(sb, r);
        }
        System.out.println(sb.toString());
    }
    
    public void outputAsData() {
        StringBuffer sb = new StringBuffer();
        for (Row r : rows.values()) {
            Iterator<Entry<String, String>> iterator = r.values.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> entry = iterator.next();
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append(", ");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }
    
}
