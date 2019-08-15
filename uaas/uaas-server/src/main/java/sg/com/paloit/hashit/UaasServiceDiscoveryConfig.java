package sg.com.paloit.hashit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile(value="with-service-discovery")
public class UaasServiceDiscoveryConfig {
    @Value(value = "${ethereum.service}")
    private String ethereumService;

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public EthereumManagementClientFactory ethereumManagementClientFactory() {
        return new EthereumManagementClientFactory(restTemplate(), ethereumService);
    }
}
