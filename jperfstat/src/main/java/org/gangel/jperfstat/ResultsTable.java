package org.gangel.jperfstat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class ResultsTable {

    public static class RowBuilder {
        private List<String> headers;
        private int headerPos = 0;
        private Row row;
        private int precision = 3; 
        
        private RowBuilder(Row row, List<String> headers) {
            this.row = row;
            this.headers = headers; 
        }
        
        public RowBuilder withPrecision(int precision) {
            this.precision = precision;
            return this; 
        }
        
        public RowBuilder add(double value) {
            return this.add(String.format("%." + Integer.toString(precision) + "f", value));
        }
        
        public RowBuilder add(String value) {
            String header = headers.get(headerPos++);
            if (header == null) {
                throw new RuntimeException(String.format("Can't add value '%s'. Missing column at this position.", value));
            }
            row.values.put(header, value);
            return this; 
        }
        
        public RowBuilder set(String key, String value) {
            if (headers.indexOf(key) < 0) {
                headers.add(key);
                ++headerPos;
            }
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
        private List<String> headers; 
        private String name; 
        private HashMap<String, String> values = new HashMap<>(); 
        private ResultsTable parentRef; 
        
        private Row(final ResultsTable parentRef, final List<String> headers, final String rowName) {
            this.parentRef = parentRef;
            this.name = rowName;
            this.headers = headers;
        }

        private RowBuilder set(String column, String value) {
            RowBuilder rb = new RowBuilder(this, headers);
            rb.set(column, value);
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
    
    private ArrayList<String> headers = new ArrayList<>();
    private LinkedHashMap<String, Row> rows = new LinkedHashMap<>();

    public ResultsTable() {
    }

    public ResultsTable addHeaders(final String... names) {
        for (String s : names) {
            headers.add(s);
        }
        return this;
    }
    
    public static ResultsTable withHeaders(final String... names) {
        ResultsTable result = new ResultsTable();
        for (String s : names) {
            result.headers.add(s);
        }
        return result;
    }

    public RowBuilder withRow(final String rowName) {
        Row row = rows.compute(rowName, (k,v) -> v == null ? new Row(this, headers, rowName) : v );
        return row.set("Name", rowName);
    }   

    private void outputCsvDataRow(StringBuffer sb, Row row) {
        for(String header : headers) {
            String value = row.values.get(header);
            sb.append(value==null ? "" : value).append(";");
        }
        sb.append("\n");
    }
    
    public void outputAsCsv() {
        StringBuffer sb = new StringBuffer();
        for(String header : headers) {
            sb.append(header==null ? "" : header).append(";");
        }
        sb.append("\n");
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
