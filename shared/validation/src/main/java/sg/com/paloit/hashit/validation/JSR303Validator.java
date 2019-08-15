package sg.com.paloit.hashit.validation;

import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class JSR303Validator {
    final private Validator validator;

    @Inject
    public JSR303Validator(final Validator validator) {
        this.validator = validator;
    }

    public boolean validate(final Class enumClass, final Set<ErrorResponseEnum> errors, final Object... objs) {
        if (objs == null) throw new IllegalArgumentException("Request array specified is null. Cannot validate null array.");
        boolean valid = true;

        for (Object obj : objs) {
            if (obj == null) throw new IllegalArgumentException("Item in array specified is null. Cannot validate null object.");
            validate(enumClass, obj, errors);
        }

        if (errors.size() > 0) {
            valid = false;
        }

        return valid;
    }

    private void validate(final Class enumClass, final Object object, final Set<ErrorResponseEnum> enums) {
        final Set<ConstraintViolation<Object>> violations = validator.validate(object);

        for (ConstraintViolation<Object> violation : violations) {
            String m = violation.getMessage();
            enums.add((ErrorResponseEnum) Enum.valueOf(enumClass, m));
        }
    }
}
