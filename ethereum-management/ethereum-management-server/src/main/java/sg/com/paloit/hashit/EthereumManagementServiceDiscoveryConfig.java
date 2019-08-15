package sg.com.paloit.hashit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile(value="with-service-discovery")
public class EthereumManagementServiceDiscoveryConfig {
    @Value("${awx.url}")
    private String awxUrl;

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate createNetworkRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AwxClientFactory ethereumNetworkClientFactory() {
        return new AwxClientFactory(createNetworkRestTemplate(), awxUrl);
    }
}
