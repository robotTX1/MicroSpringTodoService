package com.robottx.todoservice.controller;

import com.robottx.todoservice.exception.InvalidSearchQueryException;
import com.robottx.todoservice.exception.ModifyOwnershipException;
import com.robottx.todoservice.exception.NotFoundOrUnauthorizedException;
import com.robottx.todoservice.exception.ResourceCannotBeDeletedException;
import com.robottx.todoservice.exception.ResourceLimitException;
import com.robottx.todoservice.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    private static final String CLIENT_ERROR_TYPE = "ClientError";
    private static final String SERVER_ERROR_TYPE = "ServerError";

    private static final String INTERNAL_SERVER_ERROR_TITLE = "Internal Server Error";
    private static final String INTERNAL_SERVER_ERROR_DETAILS = "Something went wrong";

    @ExceptionHandler(ModifyOwnershipException.class)
    public ResponseEntity<Object> handleException(ModifyOwnershipException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(CLIENT_ERROR_TYPE)
                .title("Invalid request")
                .details(exception.getMessage())
                .status(HttpStatus.FORBIDDEN)
                .build();
        return handleAllException(errorResponse);
    }

    @ExceptionHandler(NotFoundOrUnauthorizedException.class)
    public ResponseEntity<Object> handleException(NotFoundOrUnauthorizedException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(CLIENT_ERROR_TYPE)
                .title("Resource not found or unauthorized")
                .details("The resource could not be found or you are not authorized to access it")
                .status(HttpStatus.FORBIDDEN)
                .build();
        return handleAllException(errorResponse);
    }

    @ExceptionHandler(InvalidSearchQueryException.class)
    public ResponseEntity<Object> handleException(InvalidSearchQueryException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(CLIENT_ERROR_TYPE)
                .title("Invalid search query")
                .details(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return handleAllException(errorResponse);
    }

    @ExceptionHandler(ResourceCannotBeDeletedException.class)
    public ResponseEntity<Object> handleException(ResourceCannotBeDeletedException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(CLIENT_ERROR_TYPE)
                .title("Resource exception")
                .details(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return handleAllException(errorResponse);
    }

    @ExceptionHandler(ResourceLimitException.class)
    public ResponseEntity<Object> handleException(ResourceLimitException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(CLIENT_ERROR_TYPE)
                .title("Resource limit")
                .details(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return handleAllException(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(SERVER_ERROR_TYPE)
                .title(INTERNAL_SERVER_ERROR_TITLE)
                .details(INTERNAL_SERVER_ERROR_DETAILS)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return handleAllException(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(CLIENT_ERROR_TYPE)
                .title("Unsupported Media Type")
                .details(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return handleAllException(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String details = fieldError == null ? "Failed to validate request" :
                "%s %s".formatted(fieldError.getField(), fieldError.getDefaultMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(CLIENT_ERROR_TYPE)
                .title("Failed to validate request")
                .details(details)
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return handleAllException(errorResponse);
    }

    private ResponseEntity<Object> handleAllException(ErrorResponse response) {
        return new ResponseEntity<>(response, response.getStatus());
    }


}
