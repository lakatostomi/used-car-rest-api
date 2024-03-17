package org.usedcar.rest.webservice.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.usedcar.rest.webservice.exception.EmailAlreadyExistsException;
import org.usedcar.rest.webservice.exception.ResourceDeletingIsNotAllowedException;
import org.usedcar.rest.webservice.exception.ResourceNotFoundException;
import org.usedcar.rest.webservice.exception.TokenIsRevokedException;
import org.usedcar.rest.webservice.util.HttpResponse;
import org.usedcar.rest.webservice.util.RestResponseUtil;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestExceptionController {

    @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class, MissingPathVariableException.class,  EmptyResultDataAccessException.class, EmailAlreadyExistsException.class, ResourceDeletingIsNotAllowedException.class})
    protected ResponseEntity<Object> handleClientsBadRequests(Exception ex) {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, ex.getMessage());
        String response = RestResponseUtil.createJsonStringResponse(httpResponse);
        log.warn(response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleValidations(MethodArgumentNotValidException ex) {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, createStringFromBindingResult(ex.getBindingResult()));
        String response = RestResponseUtil.createJsonStringResponse(httpResponse);
        log.warn(response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleNotExistingResources(Exception ex) {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, ex.getMessage());
        String response = RestResponseUtil.createJsonStringResponse(httpResponse);
        log.warn(response);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
        String response = RestResponseUtil.createJsonStringResponse(httpResponse);
        log.warn(response);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
        String response = RestResponseUtil.createJsonStringResponse(httpResponse);
        log.warn(response);
        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    private String createStringFromBindingResult(BindingResult result) {
        if (!result.hasFieldErrors()) {
            return result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        } else {
            return result.getFieldErrors().stream().map(error -> error.getField() + " field: " + error.getDefaultMessage()).collect(Collectors.joining("; "));
        }
    }

}
