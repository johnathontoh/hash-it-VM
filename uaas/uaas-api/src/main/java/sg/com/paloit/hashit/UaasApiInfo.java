package sg.com.paloit.hashit;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;

public class UaasApiInfo {

    public static final String API_VERSION = "v1";
    public static final String API_CONTEXT = "api";
    public static final String API_PATH = "/" + API_CONTEXT + "/" + API_VERSION;
    public static final String API_UAAS_PATH = API_PATH;
    public static final String API_AUTHENTICATE_PATH = "/authenticate";

    public static ApiInfo getInfo() {
        return new ApiInfoBuilder()
                .title("User Authentication and Administration API")
                .description("API to interact with User authentication and Authorization")
                .version(API_VERSION)
                .build();
    }
}
