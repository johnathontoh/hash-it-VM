package sg.com.paloit.hashit.validation;

public class RestException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public final BaseErrorResponse response;

    public RestException(final BaseErrorResponse response) {
        this.response = response;
    }

    public boolean containError(final ErrorResponseEnum error) {
        return response.getErrors().containsCode(error);
    }

    public BaseErrorResponse getBaseErrorResponse() {
        return response;
    }
}
