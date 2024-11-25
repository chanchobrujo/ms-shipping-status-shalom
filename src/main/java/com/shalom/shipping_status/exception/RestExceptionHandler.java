package com.shalom.shipping_status.exception;

import com.shalom.shipping_status.model.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

import static java.util.Collections.singletonMap;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.valueOf;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        return new ResponseEntity<>(singletonMap("message", ex.getMessage()), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<Map<String, String>> handleAllBusiness(BusinessException ex) {
        return new ResponseEntity<>(singletonMap("message", ex.getMessage()), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public final ResponseEntity<Map<String, Object>> handleAllBusiness(HttpClientErrorException ex) {
        Map<String, Object> responseError = ex.getResponseBodyAs(Map.class);
        return new ResponseEntity<>(responseError, valueOf(Integer.parseInt(ofNullable(responseError).map(r -> r.get("httpStatus")).map(Object::toString).orElseThrow())));
    }
}
