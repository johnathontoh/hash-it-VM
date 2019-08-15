package sg.com.paloit.hashit.security;

public class SecurityConstants {
    public static final String SECRET = "3ee9ab18-aba2-4ab4-8c1f-5b0ad7f28978";
    public static final long EXPIRATION_TIME = 3600000L; // 1 hour
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/v1/authenticate";
    public static final String CREATE_WALLET_ROUTE = "/**/createwallet";
    public static final String LOGIN_URL = "/api/v1/login";
    public static final String[] ACTUATOR_URLS = {"/trace"};
    public static final String GATEWAY_ROUTE = "/routes";
    public static final String NETWORK_DELETE_WEBHOOK = "/api/v1/networks/delete_hook";

    public static final String[] IGNORE_SWAGGER_CSRF = {
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
    };

}
