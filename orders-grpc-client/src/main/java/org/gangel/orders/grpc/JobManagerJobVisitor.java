package org.gangel.orders.grpc;

import lombok.NonNull;
import org.gangel.jperfstat.Histogram;
import org.gangel.orders.grpc.executors.CustomerRequestExecutor;
import org.gangel.orders.grpc.executors.CustomerServiceExecutor;
import org.gangel.orders.grpc.executors.OrdersRequestExecutor;
import org.gangel.orders.grpc.executors.OrdersServiceExecutor;
import org.gangel.orders.grpc.executors.ProductServiceExecutor;
import org.gangel.orders.job.Configuration;
import org.gangel.orders.job.JobManager;
import org.gangel.orders.job.JobType;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class JobManagerJobVisitor implements JobType.Visitor<JobManager> {
    
    @Override
    @NonNull
    public JobManager visitUnknown() {
        return null;
    }
    
    @Override
    @NonNull
    public JobManager visitPing() {
        return new JobManager(Configuration.jobType, new Supplier<Callable<Histogram>>() {
            @Override
            public Callable<Histogram> get() {
                return OrdersServiceExecutor.getPingExecutor();
            }
        });
    }

    @Override
    @NonNull
    public JobManager visitNewCustomer() {
        return new JobManager(Configuration.jobType, new Supplier<Callable<Histogram>>() {
            @Override
            public Callable<Histogram> get() {
                return CustomerServiceExecutor.getNewCustomerRequestExecutor();
            }
        });
    }

    @Override
    public JobManager visitGetCustomer() {
        return new JobManager(Configuration.jobType, new Supplier<Callable<Histogram>>() {
            @Override
            public Callable<Histogram> get() {
                return CustomerServiceExecutor.getGetCustomerRequestExecutor();
            }
        });
    }

    @Override
    public JobManager visitNewProduct() {
        return new JobManager(Configuration.jobType, new Supplier<Callable<Histogram>>() {
            @Override
            public Callable<Histogram> get() {
                return ProductServiceExecutor.getNewProductRequestExecutor();
            }
        });
    }

    @Override
    public JobManager visitGetProduct() {
        return new JobManager(Configuration.jobType, new Supplier<Callable<Histogram>>() {
            @Override
            public Callable<Histogram> get() {
                return ProductServiceExecutor.getGetProductRequestExecutor();
            }
        });
    }

    @Override
    public JobManager visitNewOrders() {
        return new JobManager(Configuration.jobType, new Supplier<Callable<Histogram>>() {
            @Override
            public Callable<Histogram> get() {
                return OrdersServiceExecutor.getNewOrdersRequestExecutor();
            }
        });
    }

    @Override
    public JobManager visitGetOrders() {
        return new JobManager(Configuration.jobType, new Supplier<Callable<Histogram>>() {
            @Override
            public Callable<Histogram> get() {
                return OrdersServiceExecutor.getGetOrdersRequestExecutor();
            }
        });
    }

    @Override
    public JobManager visitStreamOfNewCustomers() {
        return new JobManager(Configuration.jobType, new Supplier<Callable<Histogram>>() {

            @Override
            public Callable<Histogram> get() {
                return CustomerRequestExecutor.newCustomers();
            }
            
        });
    }

    @Override
    public JobManager visitStreamOfPings() {
        return new JobManager(Configuration.jobType, new Supplier<Callable<Histogram>>() {

            @Override
            public Callable<Histogram> get() {
                return OrdersRequestExecutor.newPingsExecutor();
            }
            
        });
    }

}
