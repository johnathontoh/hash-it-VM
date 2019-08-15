package sg.com.paloit.hashit.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import static sg.com.paloit.hashit.security.SecurityConstants.*;

@ConditionalOnProperty(value = "hashit.jwt.security", havingValue = "true")
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private Logger LOG = LoggerFactory.getLogger(WebSecurity.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, NETWORK_DELETE_WEBHOOK).permitAll()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers(CREATE_WALLET_ROUTE).permitAll()
                .antMatchers(IGNORE_SWAGGER_CSRF).permitAll()
                .antMatchers(ACTUATOR_URLS).permitAll()
                .antMatchers(GATEWAY_ROUTE).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(new JwtAuthorizationFilter(authenticationManager()), ExceptionTranslationFilter.class)
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
