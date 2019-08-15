package sg.com.paloit.hashit.validation;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {

    private List<ErrorEntry> errors = new ArrayList<>();

    public List<ErrorEntry> getErrors() {
        return errors;
    }

    public boolean containsCode(final String code) {
        for (ErrorEntry error : this.getErrors()) {
            if (code.equals(error.getCode())) {
                return true;
            }
        }
        return false;
    }

    public boolean containsCode(final ErrorResponseEnum enumeration) {
        return this.containsCode(enumeration.toString());
    }
}
