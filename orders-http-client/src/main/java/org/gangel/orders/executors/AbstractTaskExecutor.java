package org.gangel.orders.executors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.gangel.jperfstat.Histogram;
import org.gangel.orders.job.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Callable;

public abstract class AbstractTaskExecutor implements Callable<Histogram> {

    @Getter
    private Histogram histogram; 
    
    protected ObjectMapper mapper = new ObjectMapper();
    
    public abstract HttpUriRequest requestSupplier();
    
    public void responseConsumer(CloseableHttpResponse response) {        
    }
    
    protected String getProtocol() {
        return Configuration.isSSL ? "https://" : "http://";
    }
    
    protected HttpUriRequest requestGetBuilder(String path) {
        HttpGet getRequest = new HttpGet(getProtocol() + Configuration.host + ":" + Configuration.port + path);
        getRequest.addHeader("Accept", "application/json");
        return getRequest;
    }

    @SneakyThrows
    protected HttpUriRequest requestPostBuilder(String path, String body) {
        HttpPost postRequest = new HttpPost(
                    getProtocol() 
                    + Configuration.host 
                    + ":" 
                    + Configuration.port 
                    + path);
        postRequest.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        return postRequest;
    }

    private long sendRequest(CloseableHttpClient httpClient, boolean warmingPhase) throws ClientProtocolException, IOException {
        HttpUriRequest request = requestSupplier();

        long t0 = System.nanoTime();
        CloseableHttpResponse response = httpClient.execute(request);
        int code = response.getStatusLine().getStatusCode();
        if (code/100 != 2) {
            throw new RuntimeException("Unexpected response code: " + code);
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        response.getEntity().writeTo(output);
        
        long time = System.nanoTime() - t0;

        if (!warmingPhase) {
            responseConsumer(response);
        }
        response.close();
        
        return time;
    }
    
    @Override
    public Histogram call() throws Exception {
        
        histogram = new Histogram(Configuration.numOfIterations, ChronoUnit.NANOS);
        
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(Configuration.numOfIterations);
        cm.setDefaultMaxPerRoute(Configuration.numOfIterations);
        HttpHost localhost = new HttpHost(Configuration.host, Configuration.port);
        cm.setMaxPerRoute(new HttpRoute(localhost), Configuration.numOfIterations);

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        if (Configuration.numOfWarmIterations > 0) {
            for (long i = 0; i < Configuration.numOfWarmIterations; ++i) {
                sendRequest(httpClient, true);
            }
        }        
        
        histogram.setStartTime();
        for (long i = 0; i < Configuration.numOfIterations; ++i) {
            long time = sendRequest(httpClient, false);
            histogram.put(time);
        }
        histogram.setStopTime();

        httpClient.close();
        
        return histogram;

    }

}
