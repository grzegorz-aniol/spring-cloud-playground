#!/bin/sh

java -Xmx1g -Xbootclasspath/p:alpn-boot.jar -jar ./bin/orders-grpc-service-1.0-SNAPSHOT.jar --ssl.certFileName=${TEST_CERT_FILE} --ssl.privKeyFileName=${TEST_KEY_FILE} --grpc.host=$TEST_SERVER_HOST

