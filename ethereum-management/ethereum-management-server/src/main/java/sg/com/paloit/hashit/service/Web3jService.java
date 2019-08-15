package sg.com.paloit.hashit.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.crypto.WalletUtils;
import sg.com.paloit.hashit.AwxClient;
import sg.com.paloit.hashit.AwxClientFactory;
import sg.com.paloit.hashit.RestExecutor;
import sg.com.paloit.hashit.dao.entity.*;
import sg.com.paloit.hashit.dao.repository.*;
import sg.com.paloit.hashit.model.*;
import sg.com.paloit.hashit.validation.FormatException;
import sg.com.paloit.hashit.validation.ValidationMessages;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Optional.ofNullable;
import static sg.com.paloit.hashit.FileUtils.getFileContent;
import static sg.com.paloit.hashit.security.SecurityConstants.SECRET;
import static sg.com.paloit.hashit.security.SecurityConstants.TOKEN_PREFIX;

@Service
public class Web3jService {

    @Autowired
    private SmartContractJPARepository smartContractJPARepository;

    @Autowired
    private NetworkJPARepository networkJPARepository;

    @Autowired
    private TemplatesJPARepository templatesJPARepository;

    @Autowired
    private WalletJPARepository walletJPARepository;

    @Autowired
    private HashItUserJPARepository userJPARepository;

    @Autowired
    private ContractCompilerService contractCompiler;

    @Autowired
    private WalletService walletService;

    @Value("${wallet.file.location}")
    private String walletFileLocation;

    @Value("${awx.token}")
    private String awxToken;

    @Value("${contract.location}")
    private String contractLocation;

    @Autowired
    private ContractDeploymentManager contractDeploymentManager;

    @Autowired
    private ContractInteractionManager contractInteractionManager;


    @Value("${azure.subscriptionID}")
    private String subscriptionId;

    @Value("${azure.resourceGroupLocation}")
    private String resourceGroupLocation;

    Logger LOG = LoggerFactory.getLogger(Web3jService.class);
    private static final int DELAY_IN_MILLISECONDS = 60 * 1000; // 1 minute

    @PostConstruct
    public void initializeUpdateNetworkStatusTimer() {
        Timer networkStatusTimer	=	new Timer("NetworkStatus Timer");
        LOG.info("Schedule UpdateNetworkCreationStatus !");
        networkStatusTimer.schedule(new UpdateNetworkCreationStatus(), DELAY_IN_MILLISECONDS, DELAY_IN_MILLISECONDS);
    }

    public EthereumWallet createWallet(final HashItUser user) {
        String fileName;
        try {
            fileName = WalletUtils.generateFullNewWalletFile(user.getEmail(), new File(walletFileLocation));
            String walletJson = getFileContent.apply(walletFileLocation + "/" + fileName);

            WalletEntity walletEntity = WalletEntity.builder()
                    .fileName(fileName)
                    .content(walletJson)
                    .build();
            walletJPARepository.save(walletEntity);
        } catch(Exception ex) {
            LOG.info(ex.getMessage());
            throw new FormatException(ValidationMessages.WALLET_CREATION_FAILED);
        }
        return EthereumWallet.newBuilder().setWalletFileName(fileName).build();
    }

    public EthereumNetwork createNetwork(final CreateNetworkRequest createNetworkRequest, final String token) {
        String walletFileName = getWallet(token);
        HashItUserEntity user = userJPARepository.findOneByWallet(walletFileName);
        String publicAddress = "0x"+walletService.getWalletData(walletFileName).getAddress();
        CreateNetworkRequest.ExtraVars extraVars = createNetworkRequest.getExtraVars();
        extraVars.setEmailAddress(user.getEmail());
        extraVars.setEthereumAdminPublicKey(publicAddress);
        extraVars.setSubscriptionID(this.subscriptionId);
        extraVars.setResourceGroupLocation(this.resourceGroupLocation);

        return triggerNetworkPipeline(createNetworkRequest, awxToken, token);
    }

    public List<SmartContract> getSmartContracts(final String token) {
        return StreamSupport.stream(smartContractJPARepository.findAll().spliterator(), false)
                .map(this::map)
                .collect(Collectors.toList());
    }

    public List<Template> getTemplates() {
        return templatesJPARepository.findBy()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public List<EthereumNetwork> getNetworks(final String token) {
        return StreamSupport.stream(networkJPARepository.findAll().spliterator(), false)
                .map(this::map)
                .collect(Collectors.toList());
    }

    public SmartContract getSmartContract(final UUID id, final String token) {
        SmartContractEntity entity = getSmartContractEntityifAuthorised(id, token);
        return mapAll(entity);
    }

    public SmartContract deploySmartContract(final UUID id, final String token, final List<FunctionInput> input) {
        SmartContractEntity smartContractEntityifAuthorised = getSmartContractEntityifAuthorised(id, token);

        switchDeploymentStatus(smartContractEntityifAuthorised);

        SmartContract smartContract = mapAll(smartContractEntityifAuthorised);
        CompletableFuture.runAsync(() -> {
            contractCompiler.compileSolidityContract(smartContractEntityifAuthorised, smartContract.getBody());
            String contractAddress  = contractDeploymentManager.deployContract(smartContractEntityifAuthorised, token, input);
            smartContractJPARepository.save(buildSmartContract(contractAddress, smartContractEntityifAuthorised));
        });
        return smartContract;
    }

    private void switchDeploymentStatus(SmartContractEntity smartContractEntityifAuthorised) {
        smartContractEntityifAuthorised.setDeployed(false);
        smartContractJPARepository.save(smartContractEntityifAuthorised);
    }


    public Template getTemplate(final UUID id) {
        TemplateEntity entity = templatesJPARepository.findOne(id);
        return mapAll(entity);
    }

    public SmartContract saveSmartContract(final SmartContract smartContract, final String token) {
        byte[] decode = Base64.getDecoder().decode(smartContract.getBody());
        if(smartContract.getId() != null){
            SmartContractEntity savedSmartContract = smartContractJPARepository.findOne(smartContract.getId());
            if(savedSmartContract == null) {
                throw new FormatException(ValidationMessages.SMART_CONTRACT_NOT_FOUND);
            }
            SmartContractEntity entity = SmartContractEntity.builder()
                    .id(savedSmartContract.getId())
                    .address(null)
                    .wallet(savedSmartContract.getWallet())
                    .deployed(false)
                    .name(smartContract.getName())
                    .networkId(smartContract.getNetworkId())
                    .body(decode)
                    .build();
            return map(smartContractJPARepository.save(entity));
        }
        return map(smartContractJPARepository.save(convert(smartContract, token)));
    }

    public String getSmartContractMethodSpecification(final UUID id, final String token) {
        SmartContractEntity entity = getSmartContractEntityifAuthorised(id, token);
        contractCompiler.compileSolidityContract(entity, new String(entity.getBody()));
        return contractInteractionManager.getCompiledContract(entity, SmartContractManager.FILE_EXTENSION_ABI);
    }

    public Object executeMethodInContract(final UUID id, final  String token, MethodSpec methodSpec) {
        SmartContractEntity entity = getSmartContractEntityifAuthorised(id, token);
        Object interact = contractInteractionManager.interact(methodSpec, entity, token);
        return interact;
    }

    class UpdateNetworkCreationStatus extends TimerTask {

        public void run() {
            LOG.debug("Running get network status");
            try {
                networkJPARepository.findAllByStatus(NetworkStatus.CREATING)
                        .parallelStream()
                        .forEach(Web3jService.this::getNetworkStatus);
            } catch (Exception e) {
                LOG.error("Failed to get status");
                LOG.debug("failed to get status: ", e);
            }
        }
    }


    private String getWallet(String token) {
        return ofNullable(token).map(t -> {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(t.replace(TOKEN_PREFIX, ""))
                    .getBody();
            return (claims.getSubject());
        }).orElseGet(() -> "JWT");
    }

    private SmartContractEntity getSmartContractEntityifAuthorised(UUID id, String token) {
        SmartContractEntity entity = smartContractJPARepository.findOne(id);
        return entity;
    }

    private void throwExceptionIfUserIsUnathourised(final String wallet, final String token) {
        if (!StringUtils.equalsIgnoreCase(wallet, getWallet(token))) {
            throw new FormatException(ValidationMessages.USER_NOT_AUTHORIZED);
        }
    }

    private SmartContract map(final SmartContractNames smartContractNames) {
        SmartContract.SmartContractBuilder builder = SmartContract.newBuilder();
        ofNullable(smartContractNames.getId()).ifPresent(builder::setId);
        ofNullable(smartContractNames.getName()).ifPresent(builder::setName);
        ofNullable(smartContractNames.getNetworkId()).ifPresent(builder::setNetworkId);
        ofNullable(smartContractNames.getDeployed()).ifPresent(builder::setDeployed);
        ofNullable(smartContractNames.getAddress()).ifPresent(builder::setAddress);
        return builder.build();
    }

    private Template map(final TemplateNames templateNames) {
        Template.TemplateBuilder builder = Template.newBuilder();
        ofNullable(templateNames.getId()).ifPresent(builder::setId);
        ofNullable(templateNames.getName()).ifPresent(builder::setName);
        return builder.build();
    }

    private SmartContract map(final SmartContractEntity entity) {
        SmartContract.SmartContractBuilder builder = SmartContract.newBuilder();
        ofNullable(entity.getId()).ifPresent(builder::setId);
        ofNullable(entity.getName()).ifPresent(builder::setName);
        ofNullable(entity.getNetworkId()).ifPresent(builder::setNetworkId);
        ofNullable(entity.getDeployed()).ifPresent(builder::setDeployed);
        ofNullable(entity.getAddress()).ifPresent(builder::setAddress);
        return builder.build();
    }

    private EthereumNetwork map(final NetworkEntity entity) {
        EthereumNetwork.EthereumNetworkBuilder builder = EthereumNetwork.newBuilder();
        ofNullable(entity.getId()).ifPresent(builder::setId);
        ofNullable(entity.getName()).ifPresent(builder::setName);
        ofNullable(entity.getStatus()).ifPresent(builder::setStatus);
        ofNullable(entity.getRpcEndpoint()).ifPresent(builder::setRpcEndpoint);
        ofNullable(entity.getNodes()).ifPresent(builder::setNodes);
        return builder.build();
    }

    private SmartContract mapAll(final SmartContractEntity entity) {
        SmartContract.SmartContractBuilder builder = SmartContract.newBuilder();
        ofNullable(entity.getId()).ifPresent(builder::setId);
        ofNullable(entity.getName()).ifPresent(builder::setName);
        ofNullable(entity.getAddress()).ifPresent(builder::setAddress);
        ofNullable(entity.getDeployed()).ifPresent(builder::setDeployed);
        ofNullable(entity.getNetworkId()).ifPresent(builder::setNetworkId);
        ofNullable(entity.getDeployed()).ifPresent(builder::setDeployed);
        ofNullable(entity.getAddress()).ifPresent(builder::setAddress);
        ofNullable(entity.getBody()).ifPresent(p -> {
                builder.setBody(new String(p));
        });
        return builder.build();
    }

    private Template mapAll(final TemplateEntity entity) {
        Template.TemplateBuilder builder = Template.newBuilder();
        if(entity == null) return builder.build();
            ofNullable(entity.getId()).ifPresent(builder::setId);
        ofNullable(entity.getName()).ifPresent(builder::setName);
        ofNullable(entity.getJsonSchema()).ifPresent(builder::setParams);
        ofNullable(entity.getBody()).ifPresent(p -> {
                builder.setBody(new String(p)); });
        return builder.build();
    }

    private SmartContractEntity convert(final SmartContract smartContract, final String token) {
        byte[] decode = Base64.getDecoder().decode(smartContract.getBody());
        String walletString = getWallet(token);
        return SmartContractEntity
                .builder()
                .body(decode)
                .name(smartContract.getName())
                .wallet(walletString)
                .deployed(false)
                .networkId(smartContract.getNetworkId())
                .build();
    }

    private EthereumNetwork triggerNetworkPipeline(final CreateNetworkRequest createNetworkRequest,
                                                         final String awxToken, final String token) {
        CompletableFuture<EthereumNetwork> completableFuture = new CompletableFuture<>();
        final long networkCreationTimeout = 30;

        Executors.newCachedThreadPool().submit(() -> {
            AwxClient client = AwxClientFactory
                    .getInstance("job_templates/deploy-param/launch/");
            client.createNetwork(createNetworkRequest, awxToken)
                    .onSuccess(getResponse(completableFuture, createNetworkRequest, token))
                    .onError((e,d) -> completableFuture.cancel(true))
                    .execute();
        });

        try {
            return completableFuture.get(networkCreationTimeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.error("Creating network failed with: ", e);
            throw new FormatException(ValidationMessages.WALLET_CREATION_FAILED);
        }
    }

    private RestExecutor.OnSuccess<ResponseEntity> getResponse(final CompletableFuture<EthereumNetwork>
                                                                     completableFuture,
                                                               final CreateNetworkRequest createNetworkRequest,
                                                               final String token) {
        return ex -> {
            if (ofNullable(ex.getBody()).isPresent()) {
                LOG.info("Retrieved Status:- {}", ex.getBody());
                AnsibleJobLaunchResponse response = (AnsibleJobLaunchResponse)ex.getBody();
                NetworkEntity newNetwork = networkJPARepository.save(convert(createNetworkRequest, response, token));
                completableFuture.complete(map(newNetwork));
            } else {
                completableFuture.cancel(true);
            }
        };
    }

    private NetworkEntity convert(final CreateNetworkRequest createNetworkRequest, final AnsibleJobLaunchResponse
            createNetworkResponse, final String token) {
        NetworkEntity.NetworkEntityBuilder builder = NetworkEntity.newBuilder();
        ofNullable(createNetworkRequest.getNetworkName()).ifPresent(builder::setName);
        ofNullable(createNetworkRequest.getNodes()).ifPresent(builder::setNodes);
        builder.setWallet(getWallet(token));
        builder.setStatus(NetworkStatus.CREATING);
        builder.setJobId(StringUtils.replace(StringUtils.replace(createNetworkResponse.getRelated().getStdout(),
                "/api/v2/jobs/", ""), "/stdout/", ""));
        return builder.build();
    }

    public void getNetworkStatus(final NetworkEntity entity) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            AwxClient client = AwxClientFactory
                    .getInstance("jobs/" + entity.getJobId() + "/stdout/?format=txt");
            try {
                client.getNetworkStatus(awxToken)
                        .onSuccess(extractAndSaveNetworkUrl(completableFuture, entity))
                        .onError((e, d) -> completableFuture.cancel(true))
                        .execute();
            } catch (Exception e) {
                LOG.debug("Failed to execute getNetworkStatus: ", e);
                completableFuture.cancel(true);
            }
        });

        try {
            completableFuture.get(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.error("Failed to get network creation job status");
            LOG.debug("Failed to get network creation job status", e);
            throw new FormatException(ValidationMessages.WALLET_CREATION_FAILED);
        }
    }

    private RestExecutor.OnSuccess<ResponseEntity> extractAndSaveNetworkUrl(CompletableFuture<Void> completableFuture,
                                                                            NetworkEntity entity) {
        return ex -> {
            if (!ofNullable(ex.getBody()).isPresent()) {
                completableFuture.cancel(true);
            }
            // LOG.info("Retrieved Response:- {}", ex.getBody());
            String response = ex.getBody().toString();
            String regex = "\\{\\s+\"msg\":\\s+\"(http://.*.azure.com:\\d{4})\"\\s+\\}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(response);
            if (!matcher.find()) {
                completableFuture.cancel(true);
                return;
            }
            String networkUrl = matcher.group(1);
            networkJPARepository.save(NetworkEntity.newBuilder(entity)
                    .setStatus(NetworkStatus.CREATED)
                    .setRpcEndpoint(networkUrl)
                    .build());
            completableFuture.complete(null);
        };
    }

    private SmartContractEntity buildSmartContract(String contractAddress, SmartContractEntity smartContractEntity) {
        return SmartContractEntity.builder()
                .id(smartContractEntity.getId())
                .address(contractAddress)
                .body(smartContractEntity.getBody())
                .name(smartContractEntity.getName())
                .wallet(smartContractEntity.getWallet())
                .networkId(smartContractEntity.getNetworkId())
                .deployed(true)
                .build();
    }

    public void deleteNetwork(final UUID networkId, final String token) {
        NetworkEntity network = this.getNetworkEntityIfAuthorised(networkId, token);
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        final long networkDeletionTimeout = 30;

        AnsibleRequest<DeleteNetworkRequest> deleteRequest = new AnsibleRequest<>(
                new DeleteNetworkRequest(this.subscriptionId, network.getName())
        );

        Executors.newCachedThreadPool().submit(() -> {
            AwxClient client = AwxClientFactory
                .getInstance("job_templates/delete-rg/launch/");
            client.deleteNetwork(deleteRequest, awxToken)
                .onSuccess(updateNetworkStatus(completableFuture, network, NetworkStatus.DELETING))
                .onError((e, d) -> {
                    LOG.error("Failed to trigger delete network delete job:", e);
                    completableFuture.cancel(true);
                })
                .execute();
        });

        try {
            completableFuture.get(networkDeletionTimeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.error("Failed to trigger network deletion job: ", e);
            throw new FormatException(ValidationMessages.WALLET_CREATION_FAILED);
        }
    }

    private  RestExecutor.OnSuccess<ResponseEntity> updateNetworkStatus(
            CompletableFuture<Void> completableFuture,
            NetworkEntity networkEntity,
            NetworkStatus status
    ) {
        return ex -> {
            AnsibleJobLaunchResponse response = (AnsibleJobLaunchResponse)ex.getBody();
            List<SmartContractEntity> contractsToDelete = smartContractJPARepository.findAllByNetworkId(networkEntity.getId().toString());
            smartContractJPARepository.delete(contractsToDelete);
            networkJPARepository.save(
                NetworkEntity.newBuilder(networkEntity)
                    .setDeleteJobId(response.getJob().toString())
                    .setStatus(status)
                    .build()
            );
            completableFuture.complete(null);
        };
    }

    private NetworkEntity getNetworkEntityIfAuthorised(final UUID id, final String token) {
        NetworkEntity entity = networkJPARepository.findOne(id);
        throwExceptionIfUserIsUnathourised(entity.getWallet(), token);
        return entity;
    }

    public void onAnsibleNetworkDeleteNotification(final AnsibleWebhookNotification notification) {
        NetworkEntity network = networkJPARepository.findOneByDeleteJobId(notification.getId().toString());
        if (!notification.getStatus().equals(AnsibleWebhookNotification.STATUS_SUCCESS)) {
            LOG.error("AWX network deletion job failed: {}", notification);
            networkJPARepository.save(
                NetworkEntity.newBuilder(network)
                    .setStatus(NetworkStatus.ERROR)
                    .build()
            );
            return;
        }
        networkJPARepository.delete(network);
    }

    public TotalContractCount getContractsCount(final String token) {
        return new TotalContractCount(smartContractJPARepository.count());
    }

    public WalletInfo getWalletInfo(final String token) {
        String walletFileName = getWallet(token);
        String publicAddress = "0x"+walletService.getWalletData(walletFileName).getAddress();
        return new WalletInfo(publicAddress);
    }

}
