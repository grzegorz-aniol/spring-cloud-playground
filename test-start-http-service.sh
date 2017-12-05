#!/bin/sh
echo host=$TEST_SERVER_HOST

#nohup 
java -Xmx500m -Xbootclasspath/p:alpn-boot.jar -jar ./bin/orders-http-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=default,ssl --server.ssl.key-store=${TEST_KEYSTORE_NAME}.jks --server.ssl.key-store-password=${TEST_JKS_PASS} --server.ssl.key-password=${TEST_JKS_PASS} --server.host=$TEST_SERVER_HOST


## java -jar orders-http-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=default,mysql,ssl --server.ssl.key-store="c:/Users/Grzegorz_Aniol/OneDrive - EPAM/Secured/spring-cloud-playground/orders-https-service/keystore.jks" --server.ssl.key-store-password=java --server.ssl.key-password=java
