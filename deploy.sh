#!/bin/sh

echo BUILDING HASHIT BACKEND
mvn clean install -DskipTests=true

echo COPYING JARS
cp ./configuration-management/configuration-management-server-deploy/target/configuration-management-server-deploy-0.0.1-SNAPSHOT.jar ./configuration-management/docker/files/configserver.jar
cp ./service-discovery/service-discovery-server-deploy/target/service-discovery-server-deploy-0.0.1-SNAPSHOT.jar ./service-discovery/docker/files/servicediscovery.jar
cp ./api-gateway/api-gateway-server-deploy/target/api-gateway-server-deploy-0.0.1-SNAPSHOT.jar ./api-gateway/docker/files/hashitapigateway.jar
cp ./uaas/uaas-server-deploy/target/uaas-server-deploy-0.0.1-SNAPSHOT.jar ./uaas/docker/files/uaas-server.jar
cp ./ethereum-management/ethereum-management-server-deploy/target/ethereum-management-server-deploy-0.0.1-SNAPSHOT.jar ./ethereum-management/docker/files/ethereum-management-server.jar
cp ./solidityContracts/MedicalHistory.sol ./ethereum-management/docker/files/

echo DEPLOYING BUILD
 cd ./baseimage/
 if docker images | grep edge; then
     echo BASE IMAGE FOUND
 else
   docker build --tag=alpine-jdk:base .
 fi
 cd ..
docker-compose build --no-cache
docker-compose up