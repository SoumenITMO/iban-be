package com.iban.validator.exception;

import com.iban.validator.dto.APIResponse;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.context.support.DefaultMessageSourceResolvable;

/**
 * This is an exception handler class to handle mainly REST API validation errors and dispatch the error messages
 * @author Soumen Banerjee
 */

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleAllExceptions(Exception exception, Errors errors) {
        String errorMessages = errors.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" "));
        log.error(exception.getLocalizedMessage());
        return new ResponseEntity<>(new APIResponse(errorMessages), HttpStatus.BAD_REQUEST);
    }
}
