package com.scg.stop.global.exception;

import static com.scg.stop.global.exception.ExceptionCode.INVALID_REQUEST;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        log.warn(ex.getMessage(), ex);

        Map<String, Object> body = new HashMap<>();
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.toList());
        body.put("details", errors);
        body.put("code", INVALID_REQUEST.getCode());
        body.put("message", INVALID_REQUEST.getMessage());

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException ex) {

        log.warn(ex.getMessage(), ex);

        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidJwtException(InvalidJwtException ex) {

        log.warn(ex.getMessage(), ex);

        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(SocialLoginException.class)
    public ResponseEntity<ExceptionResponse> handleSocialLoginException(SocialLoginException ex) {

        log.warn(ex.getMessage(), ex);

        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(UnauthorizedException ex) {

        log.warn(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ExceptionResponse> handleInternalServerErrorException(InternalServerErrorException ex) {

        log.error(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(ex.getCode(), ex.getMessage()));
    }
}