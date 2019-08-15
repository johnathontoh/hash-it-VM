package sg.com.paloit.hashit;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sg.com.paloit.hashit.model.EthereumWallet;
import sg.com.paloit.hashit.model.HashItUser;

public class EthereumManagementClient {
    private final RestTemplate restTemplate;
    private final String SERVICE_PATH;

    public EthereumManagementClient(final String servicePath, final RestTemplate restTemplate){
        this.SERVICE_PATH = servicePath;
        this.restTemplate = restTemplate;
    }

    public RestExecutor<EthereumWallet> createWallet(final HashItUser hashItUser){
        return
                RestExecutor
                        .newRestExecutor(EthereumWallet.class)
                        .call(() -> this.createWalletResponseEntity(hashItUser));
    }

    public ResponseEntity<?> createWalletResponseEntity(final HashItUser hashItUser)
    {
        HttpEntity<HashItUser> request = new HttpEntity<>(hashItUser);
        return restTemplate.postForEntity(SERVICE_PATH, request, EthereumWallet.class);
    }
}