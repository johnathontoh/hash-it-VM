#!/bin/sh

echo GET NODES
kubectl get nodes

echo GET DEPLOYMENT
kubectl -n ${K8_NAMESPACE} get deployment configserver -o wide
kubectl -n ${K8_NAMESPACE} get deployment hashitapigateway -o wide
kubectl -n ${K8_NAMESPACE} get deployment servicediscovery -o wide
kubectl -n ${K8_NAMESPACE} get deployment ethereum-management -o wide
kubectl -n ${K8_NAMESPACE} get deployment uaas-management -o wide


echo REMOVE DEPLOYMENT
kubectl -n ${K8_NAMESPACE} delete deployment configserver
kubectl -n ${K8_NAMESPACE} delete deployment hashitapigateway 
kubectl -n ${K8_NAMESPACE} delete deployment servicediscovery
kubectl -n ${K8_NAMESPACE} delete deployment ethereum-management 
kubectl -n ${K8_NAMESPACE} delete deployment uaas-management 

echo DEPLOYMENT
kubectl -n ${K8_NAMESPACE} create -f kubernetes/configserver-deployment.yaml 
kubectl -n ${K8_NAMESPACE} create -f kubernetes/apigateway-deployment.yaml
kubectl -n ${K8_NAMESPACE} create -f kubernetes/ethereum-management-deployment.yaml 
kubectl -n ${K8_NAMESPACE} create -f kubernetes/servicediscovery-deployment.yaml
kubectl -n ${K8_NAMESPACE} create -f kubernetes/uaas-management-deployment.yaml

echo GET DEPLOYMENT
kubectl -n ${K8_NAMESPACE} get deployment configserver -o wide
kubectl -n ${K8_NAMESPACE} get deployment hashitapigateway -o wide
kubectl -n ${K8_NAMESPACE} get deployment servicediscovery -o wide
kubectl -n ${K8_NAMESPACE} get deployment ethereum-management -o wide
kubectl -n ${K8_NAMESPACE} get deployment uaas-management -o wide
