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
        T visitUnknown();
    }
    
}
