package sg.com.paloit.hashit;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableDiscoveryClient
@Profile(value="with-service-discovery")
public class EnableDiscoveryClientConfig {
}
