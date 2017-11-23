package org.gangel.orders.grpc;

import io.grpc.ServerBuilder;
import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class GRpcServerBuilderConfiguration extends GRpcServerBuilderConfigurer {

    @Autowired
    SslOptions sslOptions; 
    
    @Override
    public void configure(ServerBuilder<?> serverBuilder) {
        super.configure(serverBuilder);
        serverBuilder.useTransportSecurity(new File(sslOptions.getCertFileName()), new File(sslOptions.getPrivKeyFileName()));
    }

}
