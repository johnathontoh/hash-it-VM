package sg.com.paloit.hashit.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sg.com.paloit.hashit.EthereumManagementApi;
import sg.com.paloit.hashit.annotation.Traceable;
import sg.com.paloit.hashit.model.*;
import sg.com.paloit.hashit.service.Web3jService;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static sg.com.paloit.hashit.EthereumManagementApiInfo.*;

@RestController
@RequestMapping(path = API_ETHEREUM_PATH, produces = APPLICATION_JSON_VALUE)
public class EthereumManagementAPISringMVCRestController implements EthereumManagementApi {

    @Autowired
    private Web3jService web3jService;

    Logger LOG = LoggerFactory.getLogger(EthereumManagementAPISringMVCRestController.class);

    @Traceable
    @PostMapping(path = API_CREATE_WALLET_PATH, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    EthereumWallet createEthereumAccount(@RequestBody final HashItUser hashItUser) {
        return web3jService.createWallet(hashItUser);
    }

    @Traceable
    @PostMapping(path = API_CREATE_NETWORK_PATH, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody EthereumNetwork createNetwork(
        @RequestBody final CreateNetworkRequest createNetworkRequest,
        @RequestHeader(value = "Authorization", required = false) final String token)
    {
        return web3jService.createNetwork(createNetworkRequest, token);
    }

    @Traceable
    @DeleteMapping(path = "/networks/{networkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody void deleteNetwork(
        @PathVariable UUID networkId,
        @RequestHeader(value = "Authorization", required = false) final String token
    ) {
        web3jService.deleteNetwork(networkId, token);
    }

    @Traceable
    @GetMapping(path = API_GET_ETHEREUM_NETWORK_PATH, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<EthereumNetwork> getNetworks(@RequestHeader(value = "Authorization", required = false) final String
                                                  token) {
        return web3jService.getNetworks(token);
    }

    @Traceable
    @GetMapping(path = API_GET_SMART_CONTRACTS_PATH, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<SmartContract> getSmartContracts(@RequestHeader(value = "Authorization", required = false) final String
                                                    token) {
        return web3jService.getSmartContracts(token);
    }

    @Traceable
    @PostMapping(path = API_SAVE_SMART_CONTRACT_PATH, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    SmartContract saveSmartContract(@RequestBody final SmartContract smartContract,
                                            @RequestHeader(value = "Authorization", required = false) final String
                                                    token) {
        return web3jService.saveSmartContract(smartContract, token);
    }

    @Traceable
    @GetMapping(path = API_GET_SINGLE_SMART_CONTRACT_PATH + "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    SmartContract getSmartContract(@PathVariable(value = "id", required = true) final UUID id,
                                   @RequestHeader(value = "Authorization", required = false) final String
                                                  token) {
        return web3jService.getSmartContract(id, token);
    }

    @Traceable
    @GetMapping(path = API_GET_TEMPLATES_PATH, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<Template> getTemplates() {
        return web3jService.getTemplates();
    }

    @Traceable
    @GetMapping(path = API_GET_SINGLE_TEMPLATE_PATH + "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    Template getTemplate(@PathVariable(value = "id", required = true) final UUID id) {
        return web3jService.getTemplate(id);
    }

    @Traceable
    @PostMapping(path = API_DEPLOY_SMART_CONTRACT_PATH + "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    SmartContract deploySmartContract(@PathVariable(value = "id", required = true) final UUID id,
                                   @RequestBody final List<FunctionInput> input,
                                   @RequestHeader(value = "Authorization", required = false) final String
                                           token) {
        return web3jService.deploySmartContract(id, token, input);
    }

    @Traceable
    @GetMapping(path = API_GET_SMART_CONTRACT_METHOD_SPEC + "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String getSmartContractMethodSpecification(@PathVariable(value = "id", required = true) UUID id, @RequestHeader(value = "Authorization", required = false) String token) {
        return web3jService.getSmartContractMethodSpecification(id, token);
    }

    @Traceable
    @PostMapping(path = API_EXECUTE_SMART_CONTRACT_METHOD + "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Object executeMethodInSmartContract(@PathVariable(value = "id", required = true) final UUID id,
                                               @RequestBody final MethodSpec methodSpec,
                                               @RequestHeader(value = "Authorization", required = false) final String token) {
        return web3jService.executeMethodInContract(id, token, methodSpec);
    }



    @Traceable
    @PostMapping(path = API_DELETE_NETWORK_WEBHOOK, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    void deleteNetworkWebhook(@RequestBody final AnsibleWebhookNotification notification) {
        web3jService.onAnsibleNetworkDeleteNotification(notification);
    }

    @Traceable
    @GetMapping(path = API_GET_CONTRACTS_COUNT, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    TotalContractCount getContractsCount(@RequestHeader(value = "Authorization", required = false) final String token) {
        return web3jService.getContractsCount(token);
    }

    @Traceable
    @GetMapping(path = API_GET_WALLET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    WalletInfo getWallet(@RequestHeader(value = "Authorization", required = false) final String token) {
        return web3jService.getWalletInfo(token);
    }

}
