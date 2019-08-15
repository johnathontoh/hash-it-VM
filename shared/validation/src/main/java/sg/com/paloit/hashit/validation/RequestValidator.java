package sg.com.paloit.hashit.validation;

public interface RequestValidator {
    boolean validated();
    RestException getRestException();
}
