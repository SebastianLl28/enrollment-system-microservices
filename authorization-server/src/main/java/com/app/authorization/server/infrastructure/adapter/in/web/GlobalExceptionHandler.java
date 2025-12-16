package com.app.authorization.server.infrastructure.adapter.in.web;

import com.app.authorization.server.application.dto.response.ErrorResponse;
import com.app.authorization.server.domain.exception.InvalidCredentialsException;
import com.app.authorization.server.domain.exception.UserAlreadyExistsException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Alonso
 */
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
  
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(
    UserAlreadyExistsException ex, HttpServletRequest request) {
    
    logger.warn("Intento de registro duplicado: {}", ex.getMessage());
    
    ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
      HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(),
      ex.getMessage(), // El mensaje que lanzaste en tu servicio ("Username already exists")
      request.getRequestURI());
    
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }
  
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex,
    HttpServletRequest request) {
    
    ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
      HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
      "Credenciales inválidas", request.getRequestURI());
    
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }
  
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex,
    HttpServletRequest request) {
    
    logger.error("Error no controlado: ", ex);
    
    ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
      HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
      "Ocurrió un error interno en el servidor", request.getRequestURI());
    
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
    MethodArgumentNotValidException ex, HttpServletRequest request) {
    
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    
    logger.warn("Error de validación: {}", errors);
    
    ErrorResponse errorResponse = new ErrorResponse(
      LocalDateTime.now(),
      HttpStatus.BAD_REQUEST.value(),
      HttpStatus.BAD_REQUEST.getReasonPhrase(),
      errors.toString(),
      request.getRequestURI()
    );
    
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(
    InvalidCredentialsException ex, HttpServletRequest request) {
    
    logger.warn("Credenciales inválidas: {}", ex.getMessage());
    
    ErrorResponse errorResponse = new ErrorResponse(
      LocalDateTime.now(),
      HttpStatus.UNAUTHORIZED.value(),
      HttpStatus.UNAUTHORIZED.getReasonPhrase(),
      ex.getMessage(),
      request.getRequestURI()
    );
    
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }
}
