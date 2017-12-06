#!/bin/sh
echo OS: $OS
echo Current host: $HOSTNAME

if [ -z "$TEST_SERVER_HOST" ]; then
	echo Server host '$TEST_SERVER_HOST' is not specified. Run test-env.sh script first!
	exit 1
fi

echo Generating key pair...
SUBJECT="/CN=${TEST_SERVER_HOST}"
echo Subject: $SUBJECT
openssl req -x509 -newkey rsa:2048 -keyout $TEST_KEY_FILE -out $TEST_CERT_FILE -days 365 -nodes -subj $SUBJECT

echo Creating keystore and trust store...

echo Creating PKCS12 keystore
cat $TEST_KEY_FILE $TEST_CERT_FILE >keycert.tmp
openssl pkcs12 -export -in keycert.tmp -out $TEST_KEYSTORE_NAME.pkcs12 -name $TEST_SERVER_HOST -noiter -nomaciter -passin pass:$TEST_JKS_PASS -passout pass:$TEST_JKS_PASS

keytool -importkeystore -srckeystore $TEST_KEYSTORE_NAME.pkcs12 -destkeystore $TEST_KEYSTORE_NAME.jks -srcstoretype pkcs12 -deststoretype jks -srcstorepass $TEST_JKS_PASS -deststorepass $TEST_JKS_PASS -srcalias $TEST_SERVER_HOST -destalias $TEST_SERVER_HOST

rm keycert.tmp

read -p "Done. Press any key... "

