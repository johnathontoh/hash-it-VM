package sg.com.paloit.hashit.exception;

public class AuthorizationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AuthorizationException(String message) {
        super(message);
    }
}
