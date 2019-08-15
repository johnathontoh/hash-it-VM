package sg.com.paloit.hashit.validation;

import com.fasterxml.jackson.annotation.JsonInclude;

public class BaseErrorResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ErrorResponse errors;

    public BaseErrorResponse() {
    }

    public BaseErrorResponse(ErrorResponse errors) {
        this.errors = errors;
    }

    public ErrorResponse getErrors() {
        return errors;
    }

    public void setErrors(ErrorResponse errors) {
        this.errors = errors;
    }
}
