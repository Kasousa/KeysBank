package com.backend.keysbankapi.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleIllegalArgument(
      IllegalArgumentException ex,
      HttpServletRequest request
  ) {
    ApiError error = new ApiError(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(
      MethodArgumentNotValidException ex,
      HttpServletRequest request
  ) {
    String message = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .findFirst()
        .map(e -> e.getField() + ": " + e.getDefaultMessage())
        .orElse("Validation error");

    ApiError error = new ApiError(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        message,
        request.getRequestURI()
    );

    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneric(
      Exception ex,
      HttpServletRequest request
  ) {
    String path = request.getRequestURI();
    
    // Não tratar erros de recursos estáticos (swagger, static files, etc)
    if (path.startsWith("/swagger-ui") || 
        path.startsWith("/v3/api-docs") ||
        path.startsWith("/static/") ||
        path.startsWith("/webjars/")) {
      return null;
    }
    
    ApiError error = new ApiError(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        "Erro interno inesperado",
        path
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}