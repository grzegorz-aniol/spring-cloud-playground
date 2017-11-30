package org.gangel.orders;

import org.gangel.orders.executors.NewCustomerExecutor;
import org.gangel.orders.executors.PingExecutor;
import org.gangel.orders.job.JobManager;
import org.gangel.orders.job.JobType;

public class JobTypeVisitor implements JobType.Visitor<JobManager> {

    @Override
    public JobManager visitPing() {
        return new JobManager(JobType.PING, () -> {
            return new PingExecutor();
        });
    }

    @Override
    public JobManager visitNewCustomer() {
        return new JobManager(JobType.NEWCUSTOMER, () -> {
            return new NewCustomerExecutor();
        });
    }

    @Override
    public JobManager visitGetCustomer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JobManager visitNewProduct() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JobManager visitGetProduct() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JobManager visitNewOrders() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JobManager visitGetOrders() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JobManager visitUnknown() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JobManager visitStreamOfNewCustomers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JobManager visitStreamOfPings() {
        // TODO Auto-generated method stub
        return null;
    }

}
