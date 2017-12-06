#!/bin/sh

java -Xmx1g -Xbootclasspath/p:alpn-boot.jar -jar ./bin/orders-http-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=default,ssl --server.ssl.key-store=${TEST_KEYSTORE_NAME}.jks --server.ssl.key-store-password=${TEST_JKS_PASS} --server.ssl.key-password=${TEST_JKS_PASS} --server.host=$TEST_SERVER_HOST

