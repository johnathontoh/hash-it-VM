package sg.com.paloit.hashit.validation;

import java.util.HashSet;
import java.util.Set;

public class BaseRequestValidator<Req> implements RequestValidator {
    final JSR303Validator jsr303Validator;
    private Set<ErrorResponseEnum> errors = new HashSet<>();
    private Class<? extends ErrorResponseEnum> enumClass;
    private Req request;

    public BaseRequestValidator(final JSR303Validator jsr303Validator,
                                final Class<? extends ErrorResponseEnum> enumClass, final Req request) {
        this.jsr303Validator = jsr303Validator;
        this.enumClass = enumClass;
        this.request = request;
    }

    public Set<ErrorResponseEnum> getErrors() {
        return errors;
    }

    public Req getRequest() {
        return this.request;
    }

    @Override
    public boolean validated() {
        return jsr303Validator.validate(enumClass, errors, request);
    }

    @Override
    public RestException getRestException() {
        return new FormatException(getErrors());
    }

    public void addError(ErrorResponseEnum error) {
        errors.add(error);
    }
}
