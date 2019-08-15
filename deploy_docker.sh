#!/bin/sh
set -e

echo BUILDING HASHIT BACKEND

mvn clean install -DskipTests=true

echo COPYING JARS
cp ./configuration-management/configuration-management-server-deploy/target/configuration-management-server-deploy-0.0.1-SNAPSHOT.jar ./configuration-management/docker/files/configserver.jar
cp ./configuration-management/configuration-management-server-deploy/target/configuration-management-server-deploy-0.0.1-SNAPSHOT.jar ./configuration-management/docker/files/configserver.jar
cp ./service-discovery/service-discovery-server-deploy/target/service-discovery-server-deploy-0.0.1-SNAPSHOT.jar ./service-discovery/docker/files/servicediscovery.jar
cp ./api-gateway/api-gateway-server-deploy/target/api-gateway-server-deploy-0.0.1-SNAPSHOT.jar ./api-gateway/docker/files/hashitapigateway.jar
cp ./uaas/uaas-server-deploy/target/uaas-server-deploy-0.0.1-SNAPSHOT.jar ./uaas/docker/files/uaas-server.jar
cp ./ethereum-management/ethereum-management-server-deploy/target/ethereum-management-server-deploy-0.0.1-SNAPSHOT.jar ./ethereum-management/docker/files/ethereum-management-server.jar
cp ./solidityContracts/MedicalHistory.sol ./ethereum-management/docker/files/



echo BUILD DOCKER IMAGES
docker-compose build --no-cache


echo CHECK DOCKER IMAGES
docker images
docker ps -a

echo TAG AND PUSH TO DOCKER REGISTRY
docker tag configserver docker.paloitcloud.com.sg/hash-it/configserver:latest
docker push docker.paloitcloud.com.sg/hash-it/configserver:latest


docker tag hashitapigateway docker.paloitcloud.com.sg/hash-it/hashitapigateway:latest
docker push docker.paloitcloud.com.sg/hash-it/hashitapigateway:latest


docker tag servicediscovery docker.paloitcloud.com.sg/hash-it/servicediscovery:latest
docker push docker.paloitcloud.com.sg/hash-it/servicediscovery:latest


docker tag ethereum-management docker.paloitcloud.com.sg/hash-it/ethereum-management:latest
docker push docker.paloitcloud.com.sg/hash-it/ethereum-management:latest


docker tag uaas-management docker.paloitcloud.com.sg/hash-it/uaas-management:latest
docker push docker.paloitcloud.com.sg/hash-it/uaas-management:latest
