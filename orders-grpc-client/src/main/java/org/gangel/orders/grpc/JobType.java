package org.gangel.orders.grpc;

import lombok.Getter;

public enum JobType  {

    UNKNOWN("Unknown") {
        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnknown();
        }
    },
    
    PING("Ping") {
        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitPing();
        }
    }, 
    
    NEWCUSTOMER("New customer") {
        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitNewCustomer();
        }
    },
    
    GETCUSTOMER("Get customer") {
        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitGetCustomer();
        }
    },
    
    NEWPRODUCT("New product") {
        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitNewProduct();
        }
    },
    
    GETPRODUCT("Get product") {
        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitGetProduct();
        }
    }, 
    
    NEWORDERS("New orders") {
        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitNewOrders();
        }
    },
    
    GETORDERS("Get orders") {
        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitGetOrders();
        }
    }
    ;
    
    JobType(String name) {
        this.name = name; 
    }

    @Getter
    private String name;
    
    public abstract <T> T accept(Visitor<T> visitor);
    
    interface Visitor<T> {
        T visitPing();
        T visitNewCustomer();
        T visitGetCustomer();
        T visitNewProduct();
        T visitGetProduct();
        T visitNewOrders();
        T visitGetOrders();
        T visitUnknown();
    }
    
}
