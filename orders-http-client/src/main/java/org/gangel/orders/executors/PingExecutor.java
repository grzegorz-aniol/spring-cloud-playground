package org.gangel.orders.executors;

import org.apache.http.client.methods.HttpUriRequest;

public class PingExecutor extends AbstractTaskExecutor {

    @Override
    public HttpUriRequest requestSupplier() {
        return requestGetBuilder("/");
    }

}
