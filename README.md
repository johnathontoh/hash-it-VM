# HASHIT-BACKEND-VM (Needs to be edited)
Initial Microservice list

1. Configuration Management - This microservice configuration/properties for all other microservices

2. Service Discovery - http://localhost:8761

3. HashItApiGateway - Reverse proxy to redirect UI calls to appropriate microservices -- localhost:8080

4. Uaas - User Authentication And Authorization Service - http://localhost:1025
   
   - Swagger Url - http://localhost:1025/swagger-ui.html#/uaas
   
5. Ethereum-Management - Ethereum Management - http://localhost:1027
   
   - Swagger Url - http://localhost:1027/swagger-ui.html#/ethereum-management

Note: all domain names and port number will/may change as it goes to production.

# Run

You need to run micro service in this order:
1. ConfigurationManagementServer
2. ServiceDiscoveryServer
3. UaasServer
4. EthereumManagementServer
5. HashItReverseProxyServer

## Ethereum management

You need to edit `ethereum-management.properties` (need to restart ConfigurationManagementServer afetr editing the file).
And change `contract.location` to the path of the `solidityContracts` of this project.
Example:
```properties
contract.location=/Users/<your_login>/Code/hash-it/solidityContracts/
```

On every startup EthereumManagement server will update the templates (.sol and .json) stored in the database with the 
local copy situated in the the `solidityContracts` folder of this repository.

- EthereumManagementAPISringMVCRestController is the MVC controller containing all routes served by EthereumManagement.
- Web3jService is the service handling most of the called received by EthereumManagementAPISringMVCRestController.

# DEPLOY

Run ./deploy.sh -- This should do a maven clean install. copy all jars inside respective docker folder.
                   If base image not found, a docker base image will be built and then we will do a
                   docker-compose up --build
                   

# DEPLOY KUBERNETES

The Deployment of Kubernetes use the Jenkins pipeline library: https://github.com/paloitsingapore/hash-it-pipeline-library/tree/master/vars
The images are uploaded into Nexus (https://nexus.paloitcloud.com.sg/) then deployed into Kubernetes

The steps are:

1. Create image and push it into Nexus

   - Jenkins stage: Docker Build & Push
   - Description: Use a generic container of Ubuntu with Maven, docker and docker compose to compile, build and push the containers created

2. Deploy into Kubernetes (Dev)

   - Jenkins stage: Kubernetes
   - Description: Use a generic container with Kubectl to deploy the images into hash-it dev namespace

# Sign Up Url

 - http://localhost:8080/uaas/api/v1/register
 - Model given in uaas swagger
 
# Login Url

 - http://localhost:8080/uaas/api/v1/login
 - Model same as register api model
 
# DATABASE

We are using a hosted Oracle database.
The following properties need to be configures in `ethereum-management.properties` and `uaas.properties` 
in the configuration management micro service.

```properties
spring.jpa.database-platform=org.hibernate.dialect.Oracle10gDialect
spring.datasource.url=jdbc:oracle:thin:@//<host>:<port>/<db>
spring.datasource.username=<Username>
spring.datasource.password=<password>
spring.datasource.driver-class-name=oracle.jdbc.driver.OracleDriver
spring.jpa.properties.hibernate.default_schema=<Schema>
spring.jpa.database=oracle
spring.jpa.hibernate.ddl-auto=none
```

# LIQUIBASE

Liquibase should be used for schema creation. Once liquibase is
in place, remove import.sql from ethereum-management-server/resources folder.
The import.sql for now is inserting templates data in H2 db. 

Also remove the MedicalRecords.sol from the ethereum docker folder and the copy 
command from the dockerfile.
    
# To install solc 0.4.26 in mac (excat version is needed to be compatible with the contracts (0.4.25) we are using)

Follow instruction from [this](https://medium.com/@zulhhandyplast/how-to-install-solidity-0-4-x-on-mac-using-homebrew-8dfadb244f5d).
