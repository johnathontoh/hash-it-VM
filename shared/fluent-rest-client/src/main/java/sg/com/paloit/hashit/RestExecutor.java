package sg.com.paloit.hashit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import sg.com.paloit.hashit.validation.ErrorResponse;
import sg.com.paloit.hashit.validation.FormatException;
import sg.com.paloit.hashit.validation.ValidationMessages;

import java.util.Optional;

public class RestExecutor<Req> {
    private OnSuccess<ResponseEntity> onSucess;
    private OnError onError;
    private RestCall<ResponseEntity, Req> restCall;
    final ObjectMapper json = new ObjectMapper();

    public RestExecutor<Req> call(final RestCall<ResponseEntity, Req> restCall) {
        this.restCall = restCall;
        return this;
    }

    public RestExecutor<Req> onSuccess(final OnSuccess<ResponseEntity> onSuccess) {
        this.onSucess = onSuccess;
        return this;
    }

    public RestExecutor<Req> onError(final OnError onError) {
        this.onError = onError;
        return this;
    }

    public Result execute() throws HttpStatusCodeException {
        try {
            final ResponseEntity response = restCall.execute();
            Optional.ofNullable(onSucess)
                    .ifPresent(s -> s.success(response));
            return Result.SUCCESS;
        } catch(final HttpStatusCodeException error) {
            Optional.ofNullable(onError)
                .ifPresent(e -> {
                    ErrorResponse errorResponse;
                    final String response = new String(error.getResponseBodyAsByteArray());
                    try {
                        if (!StringUtils.isEmpty(response)){
                            errorResponse = json.readValue(response, ErrorResponse.class);
                            e.error(error, errorResponse);
                        }
                    } catch(Exception ioe) {
                        throw new FormatException(ValidationMessages.FAILED_TO_PARSE_JSON_RESPONSE);
                    }
                });
            return Result.FAILED;
        }
    }

    public static <Req> RestExecutor<Req> newRestExecutor(Class<Req> requestClass) {
        return new RestExecutor<>();
    }

    public interface RestCall<Res, Req> {
        Res execute();
    }

    public interface OnSuccess<Res> {
        void success(final Res t);
    }

    public interface OnError {
        void error(final HttpStatusCodeException error, final ErrorResponse errorResponse);
    }

    public enum Result {
        SUCCESS, FAILED
    }
}
