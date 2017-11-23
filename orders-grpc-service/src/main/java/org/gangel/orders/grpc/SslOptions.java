package org.gangel.orders.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="ssl")
public class SslOptions {

    private String certFileName;
    
    private String privKeyFileName;

    public String getCertFileName() {
        return certFileName;
    }

    public void setCertFileName(String certFileName) {
        this.certFileName = certFileName;
    }

    public String getPrivKeyFileName() {
        return privKeyFileName;
    }

    public void setPrivKeyFileName(String privKeyFileName) {
        this.privKeyFileName = privKeyFileName;
    } 
}
