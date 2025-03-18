package com.robottx.todoservice.controller;

import com.robottx.todoservice.exception.ResourceCannotBeDeletedException;
import com.robottx.todoservice.model.ErrorResponse;
import com.robottx.todoservice.exception.InvalidSearchQueryException;
import com.robottx.todoservice.exception.NotFoundOrUnauthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    private static final String CLIENT_ERROR_TYPE = "ClientError";
    private static final String SERVER_ERROR_TYPE = "ServerError";

    private static final String INTERNAL_SERVER_ERROR_TITLE = "Internal Server Error";
    private static final String INTERNAL_SERVER_ERROR_DETAILS = "Something went wrong";

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

    private ResponseEntity<Object> handleAllException(ErrorResponse response) {
        return new ResponseEntity<>(response, response.getStatus());
    }


}
