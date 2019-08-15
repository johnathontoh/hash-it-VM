package sg.com.paloit.hashit.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sg.com.paloit.hashit.UaasApi;
import sg.com.paloit.hashit.annotation.Traceable;
import sg.com.paloit.hashit.controller.validation.RequestValidatorFactory;
import sg.com.paloit.hashit.model.HashItUser;
import sg.com.paloit.hashit.service.AuthenticationService;
import sg.com.paloit.hashit.validation.RequestValidator;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static sg.com.paloit.hashit.UaasApiInfo.API_AUTHENTICATE_PATH;
import static sg.com.paloit.hashit.UaasApiInfo.API_UAAS_PATH;
import static sg.com.paloit.hashit.security.SecurityConstants.*;

@RestController
@RequestMapping(path = API_UAAS_PATH, produces = APPLICATION_JSON_VALUE)
public class UaasAPISringMVCRestController implements UaasApi {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RequestValidatorFactory validatorFactory;

    private static final Logger LOG = LoggerFactory.getLogger(UaasAPISringMVCRestController.class);

    @Traceable
    @PostMapping(path = API_AUTHENTICATE_PATH, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    ResponseEntity<HashItUser> authenticate(@RequestBody final HashItUser hashItUser) {
        LOG.info("Calling authenticate end point");
        final RequestValidator validator = validatorFactory.validatorInstance(hashItUser);
        if (validator.validated()) {
            return formAuthenticationResponse(authenticationService.register(hashItUser));
        } else {
            throw validator.getRestException();
        }
    }

    private String getJwtToken(final String wallet) {
        return Jwts.builder()
                .setSubject(wallet)
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
    }

    private ResponseEntity<HashItUser> formAuthenticationResponse(final HashItUser hashItUser) {
        return ResponseEntity.ok()
                .header(HEADER_STRING, TOKEN_PREFIX +
                        getJwtToken(hashItUser.getWallet()))
                .body(hashItUser);
    }
}
