package sg.com.paloit.hashit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sg.com.paloit.hashit.model.AnsibleRequest;
import sg.com.paloit.hashit.model.CreateNetworkRequest;
import sg.com.paloit.hashit.model.AnsibleJobLaunchResponse;
import sg.com.paloit.hashit.model.DeleteNetworkRequest;

public class AwxClient {
    private final RestTemplate restTemplate;
    private final String SERVICE_PATH;

    Logger LOG = LoggerFactory.getLogger(AwxClient.class);

    public AwxClient(final String servicePath, final RestTemplate restTemplate){
        this.SERVICE_PATH = servicePath;
        this.restTemplate = restTemplate;
    }

    public RestExecutor<AnsibleJobLaunchResponse> createNetwork(final CreateNetworkRequest createNetworkRequest,
                                                                final String awxToken){
        return
                RestExecutor
                        .newRestExecutor(AnsibleJobLaunchResponse.class)
                        .call(() -> this.createNetworkResponseEntity(createNetworkRequest, awxToken));
    }

    public ResponseEntity<?> createNetworkResponseEntity(final CreateNetworkRequest createNetworkRequest,
                                                         final String awxToken)
    {
        HttpEntity<CreateNetworkRequest> request = new HttpEntity<>(createNetworkRequest,
                getAuthorizationHeader(awxToken));
        return restTemplate.exchange
                (SERVICE_PATH, HttpMethod.POST, request, AnsibleJobLaunchResponse.class);
    }

    public RestExecutor<String> getNetworkStatus(final String awxToken){
        return
                RestExecutor
                        .newRestExecutor(String.class)
                        .call(() -> this.getNetworkStatusResponseEntity(awxToken));
    }

    public ResponseEntity<?> getNetworkStatusResponseEntity(final String awxToken)
    {
        HttpEntity<Void> request = new HttpEntity<>(null, getAuthorizationHeader(awxToken));
        return restTemplate.exchange
                (SERVICE_PATH, HttpMethod.GET, request, String.class);
    }

    private HttpHeaders getAuthorizationHeader(final String awxToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + awxToken);
        headers.add("Content-type", "application/json");
        return headers;
    }

    public RestExecutor<AnsibleJobLaunchResponse> deleteNetwork(
            final AnsibleRequest<DeleteNetworkRequest> deleteNetworkRequest,
            final String awxToken
    ){
        return RestExecutor
                .newRestExecutor(AnsibleJobLaunchResponse.class)
                .call(() -> this.deleteNetworkResponseEntity(deleteNetworkRequest, awxToken));
    }

    public ResponseEntity<?> deleteNetworkResponseEntity(final AnsibleRequest<DeleteNetworkRequest> deleteNetworkRequest,
                                                         final String awxToken)
    {
        HttpEntity<AnsibleRequest<DeleteNetworkRequest>> request = new HttpEntity<>(deleteNetworkRequest,
                getAuthorizationHeader(awxToken));
        return restTemplate.exchange
                (SERVICE_PATH, HttpMethod.POST, request, AnsibleJobLaunchResponse.class);
    }
}