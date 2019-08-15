package sg.com.paloit.hashit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class EthereumManagementServer extends SpringBootServletInitializer{

    @Override
    public SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EthereumManagementServer.class);
    }

    public static void main(final String args[]) {
        SpringApplication.run(EthereumManagementServer.class, args);
    }

}
