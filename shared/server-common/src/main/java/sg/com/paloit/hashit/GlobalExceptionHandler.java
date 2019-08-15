package sg.com.paloit.hashit;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sg.com.paloit.hashit.exception.AuthorizationException;
import sg.com.paloit.hashit.exception.ErrorMessage;
import sg.com.paloit.hashit.validation.FormatException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = FormatException.class)
    protected ResponseEntity<Object> handleFormatException(final HttpServletRequest request, final FormatException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(ex.getBaseErrorResponse().getErrors());
    }

    @ExceptionHandler(value = AuthorizationException.class)
    protected ResponseEntity<Object> handleAuthorizationException(final HttpServletRequest request, final FormatException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(new ErrorMessage(ex.getMessage(), ex.toString(), request.getRequestURI(), request.getMethod()));
    }

    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<Object> handleFormatException(final HttpServletRequest request, final RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(new ErrorMessage(ex.getMessage(), ex.toString(), request.getRequestURI(), request.getMethod()));
    }
}
