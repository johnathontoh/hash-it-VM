package sg.com.paloit.hashit.controller.validation;

import org.springframework.stereotype.Component;
import sg.com.paloit.hashit.model.CreateNetworkRequest;
import sg.com.paloit.hashit.validation.JSR303Validator;
import sg.com.paloit.hashit.validation.RequestValidator;

import javax.inject.Inject;


@Component
public class RequestValidatorFactory{

    private JSR303Validator jsr303Validator;

    @Inject
    public RequestValidatorFactory(final JSR303Validator jsr303Validator){
        this.jsr303Validator = jsr303Validator;
    }

    @SuppressWarnings("unchecked")
    public <REQ> RequestValidator validatorInstance(final REQ request) {
        if(request == null)throw new IllegalArgumentException("Null request cannot be mapped to validator type");
        if(request.getClass() == CreateNetworkRequest.class){
            return new CreateNetworkRequestValidator(jsr303Validator, (CreateNetworkRequest) request);
        } else {
            throw new IllegalArgumentException("Unknown request cannot be mapped to validator [" + request.getClass()
                    .getCanonicalName() + "]");
        }
    }


    public void performValidation(Object request) {
        final RequestValidator validator = validatorInstance(request);
        if(!validator.validated()) {
            throw validator.getRestException();
        }
    }
}
