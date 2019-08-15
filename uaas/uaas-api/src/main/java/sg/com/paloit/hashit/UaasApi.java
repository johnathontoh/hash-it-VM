package sg.com.paloit.hashit;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import sg.com.paloit.hashit.model.HashItUser;
import sg.com.paloit.hashit.validation.ErrorResponse;

import java.net.HttpURLConnection;

@Api(
        value = "uaas-api",
        description = "Interface for user authentication and authorization",
        tags = {"uaas"}
)
public interface UaasApi {
    @ApiOperation(value = "Authenticate user",
            notes = "Authenticate user")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_CREATED, message = "Registered new user"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, response = ErrorResponse.class,
                    message = "Error Response"),
    })
    ResponseEntity<HashItUser> authenticate(@ApiParam(value = "Application User To Register", required = true)
                                        final HashItUser hashItUser);
}
