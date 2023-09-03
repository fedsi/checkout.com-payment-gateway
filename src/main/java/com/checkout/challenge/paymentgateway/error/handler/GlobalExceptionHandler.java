package com.checkout.challenge.paymentgateway.error.handler;

import com.checkout.challenge.paymentgateway.error.RestApiError;
import com.checkout.challenge.paymentgateway.error.exception.ExistingPaymentException;
import com.checkout.challenge.paymentgateway.error.message.ExceptionMessages;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String REQUEST_TYPE_EXCEPTION = "request";

    private ResponseEntity<Object> buildResponseEntity(RestApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(ExistingPaymentException.class)
    protected ResponseEntity<Object> handlePaymentAlreadyExist(ExistingPaymentException ex) {
        RestApiError apiError = new RestApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setType(REQUEST_TYPE_EXCEPTION);
        apiError.setMessage(ex.getMessage());
        apiError.setErrors(Arrays.asList(String.format(ex.getError(), ex.getPaymentId())));
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RestApiError apiError = new RestApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setType(REQUEST_TYPE_EXCEPTION);
        apiError.setMessage(ExceptionMessages.PAYMENT_REQUEST_VALIDATION_MESSAGE);

        // Get all errors
        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        apiError.setErrors(errors);
        return buildResponseEntity(apiError);
    }
}
