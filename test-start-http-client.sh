#!/bin/sh
java -Xmx1g -cp ./bin/orders-http-client-1.0-SNAPSHOT.jar org.gangel.orders.OrdersHttpClient -j $* -t 1 -i 1000 -wi 5 -h ${TEST_SERVER_HOST} -p 8010 --ssl --cert ${TEST_KEYSTORE_NAME}.jks

