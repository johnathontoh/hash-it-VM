package sg.com.paloit.hashit;

import org.springframework.web.client.RestTemplate;

public class EthereumManagementClientFactory {
    private static RestTemplate template;

    private static String ethereumManagementService;

    public EthereumManagementClientFactory(final RestTemplate template, final String ethereumManagementService){
        EthereumManagementClientFactory.template = template;
        EthereumManagementClientFactory.ethereumManagementService = ethereumManagementService;
    }

    public static EthereumManagementClient getInstance(String apiName){
        return new EthereumManagementClient(ethereumManagementService + apiName
                , template);
    }
}
