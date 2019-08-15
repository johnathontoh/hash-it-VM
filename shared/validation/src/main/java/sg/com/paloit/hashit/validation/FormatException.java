package sg.com.paloit.hashit.validation;

import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FormatException extends RestException {
    public FormatException(final ErrorResponseEnum error) {
        super(new SimpleErrorResponse(new ErrorResponseBuilder().addError(error).build()));
    }

    public FormatException(final Set<ErrorResponseEnum> errors) {
        super(new SimpleErrorResponse(new ErrorResponseBuilder().addErrors(errors).build()));
    }

    public static FormatException build(List<ObjectError> allErrors) {
        Set<ErrorResponseEnum> set = allErrors.stream()
                .map(FormatException::convert)
                .collect(Collectors.toSet());

        return new FormatException(set);
    }

    private static ErrorResponseEnum convert(ObjectError objectError) {
        return objectError::getDefaultMessage;
    }
}
