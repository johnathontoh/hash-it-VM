package sg.com.paloit.hashit.controller.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sg.com.paloit.hashit.model.CreateNetworkRequest;
import sg.com.paloit.hashit.validation.BaseRequestValidator;
import sg.com.paloit.hashit.validation.JSR303Validator;
import sg.com.paloit.hashit.validation.ValidationMessages;

import javax.inject.Inject;

public class CreateNetworkRequestValidator extends BaseRequestValidator<CreateNetworkRequest> {
    private static final Logger LOG = LoggerFactory.getLogger(CreateNetworkRequestValidator.class);

    @Inject
    public CreateNetworkRequestValidator(final JSR303Validator jsr303Validator, final CreateNetworkRequest request) {
        super(jsr303Validator, ValidationMessages.class, request);
    }


    @Override
    public boolean validated() {
        boolean validated = true;
        if (super.validated()) {
            if (getRequest().getNodes() == 0) {
                addError(ValidationMessages.NUMBER_OF_NODES_CANNOT_BE_ZERO);
                validated = false;
            }
        } else {
            validated = false;
        }
        return validated;
    }
}
