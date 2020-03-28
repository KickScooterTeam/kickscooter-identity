package com.softserve.identityservice.handler;

import com.softserve.identityservice.exception.AuthorizationException;
import com.softserve.identityservice.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.ServletException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(Exception e, WebRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .time(LocalDateTime.now()).error(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ErrorResponse> servletException(Exception e, WebRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .time(LocalDateTime.now()).error(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> authorizationException(Exception e, WebRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .time(LocalDateTime.now()).error(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse,HttpStatus.FORBIDDEN);
    }
}
