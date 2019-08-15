package sg.com.paloit.hashit;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;

public class EthereumManagementApiInfo {

    public static final String API_VERSION = "v1";
    public static final String API_CONTEXT = "api";
    public static final String API_PATH = "/" + API_CONTEXT + "/" + API_VERSION;
    public static final String API_ETHEREUM_PATH = API_PATH;
    public static final String API_CREATE_WALLET_PATH = "/createwallet";
    public static final String API_CREATE_NETWORK_PATH = "/createnetwork";
    public static final String API_GET_SMART_CONTRACTS_PATH = "/smartcontracts";
    public static final String API_GET_ETHEREUM_NETWORK_PATH = "/networks";
    public static final String API_GET_SINGLE_SMART_CONTRACT_PATH = "/smartcontract";
    public static final String API_SAVE_SMART_CONTRACT_PATH = "/smartcontract/save";
    public static final String API_DEPLOY_SMART_CONTRACT_PATH = "/deploy";
    public static final String API_GET_TEMPLATES_PATH = "/templates";
    public static final String API_GET_SINGLE_TEMPLATE_PATH = "/template";
    public static final String API_GET_SMART_CONTRACT_METHOD_SPEC = "/methodspec";
    public static final String API_EXECUTE_SMART_CONTRACT_METHOD = "/execute";
    public static final String API_DELETE_NETWORK_WEBHOOK = "/networks/delete_hook";
    public static final String API_GET_CONTRACTS_COUNT = "/smartcontract/count";
    public static final String API_GET_WALLET = "/wallet";

    public static ApiInfo getInfo() {
        return new ApiInfoBuilder()
                .title("Ethereum Management API")
                .description("API to interact with Ethereum Management")
                .version(API_VERSION)
                .build();
    }
}
