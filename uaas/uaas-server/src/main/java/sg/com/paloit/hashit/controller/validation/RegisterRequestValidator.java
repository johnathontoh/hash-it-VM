package sg.com.paloit.hashit.controller.validation;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sg.com.paloit.hashit.model.HashItUser;
import sg.com.paloit.hashit.validation.BaseRequestValidator;
import sg.com.paloit.hashit.validation.JSR303Validator;
import sg.com.paloit.hashit.validation.ValidationMessages;

import javax.inject.Inject;

public class RegisterRequestValidator extends BaseRequestValidator<HashItUser> {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterRequestValidator.class);

    @Inject
    public RegisterRequestValidator(final JSR303Validator jsr303Validator, final HashItUser request) {
        super(jsr303Validator, ValidationMessages.class, request);
    }


    @Override
    public boolean validated() {
        boolean validated = true;
        if (super.validated()) {
            if (StringUtils.isBlank(getRequest().getUserId())) {
                addError(ValidationMessages.USER_ID_IS_MANDATORY);
                validated = false;
            }
        } else {
            validated = false;
        }
        return validated;
    }
}
