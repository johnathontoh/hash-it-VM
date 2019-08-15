package sg.com.paloit.hashit;

import org.springframework.web.client.RestTemplate;

public class AwxClientFactory {
    private static RestTemplate template;

    private static String awxServiceUrl;

    public AwxClientFactory(final RestTemplate template, final String awxServiceUrl){
        AwxClientFactory.template = template;
        AwxClientFactory.awxServiceUrl = awxServiceUrl;
    }

    public static AwxClient getInstance(String apiName){
        return new AwxClient(awxServiceUrl + apiName
                , template);
    }
}
