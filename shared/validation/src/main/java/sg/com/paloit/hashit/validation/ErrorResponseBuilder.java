package sg.com.paloit.hashit.validation;

import java.util.HashSet;
import java.util.Set;

public class ErrorResponseBuilder {
    final Set<ErrorResponseEnum> enums = new HashSet<>();

    public ErrorResponseBuilder addErrors(final Set<ErrorResponseEnum> errors) {
        enums.addAll(errors);
        return this;
    }

    public ErrorResponseBuilder addError(final ErrorResponseEnum e) {
        enums.add(e);
        return this;
    }

    public ErrorResponse build() {
        ErrorResponse errorResponse = null;

        if (enums != null){
            errorResponse = new ErrorResponse();

            for (final ErrorResponseEnum e: enums) {
                ErrorEntry errorEntry = getErrorEntry(e);
                errorResponse.getErrors().add(errorEntry);
            }
        }

        return errorResponse;
    }

    private static ErrorEntry getErrorEntry(final ErrorResponseEnum e) {
        ErrorEntry errorEntry;
        errorEntry = new ErrorEntry();
        errorEntry.setCode(e.toString());
        errorEntry.setSystemMessage(e.getSystemMessage());
        return errorEntry;
    }
}
