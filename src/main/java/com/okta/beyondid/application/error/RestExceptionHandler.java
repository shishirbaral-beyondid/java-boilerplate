/*

package com.okta.beyondid.application.error;

import com.okta.sdk.resource.ResourceException;
import io.swagger.annotations.Api;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiErrorDto(HttpStatus.BAD_REQUEST, error, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErrorDto apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(ResourceException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            ResourceException ex) {
        ApiErrorDto apiError = new ApiErrorDto();
        switch (ex.getStatus()){
            case 404:
                apiError.setStatus(HttpStatus.NOT_FOUND);

                break;
            default:
                apiError.setStatus(HttpStatus.BAD_REQUEST);
                break;

        }
        if(ex.getCauses()!=null && ex.getCauses().size()>1){
            apiError.setCauses(ex.getCauses().stream().map(cause->cause.getSummary()).collect(Collectors.toList()));
        }
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.getLocalizedMessage());

        return buildResponseEntity(apiError);
    }
}

*/
