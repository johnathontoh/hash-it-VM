#!/bin/sh

while ! nc -z configserver 8888 ; do
	echo "Waiting for config server to boot up"
	sleep 5
done

java $JAVA_OPTIONS -jar /opt/lib/ethereum-management-server.jar