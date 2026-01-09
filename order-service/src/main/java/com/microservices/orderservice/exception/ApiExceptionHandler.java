package com.microservices.orderservice.exception;

import com.microservices.orderservice.model.dto.OutOfStockItemsDto;
import com.microservices.orderservice.model.response.DefaultResponse;
import com.microservices.orderservice.model.response.ErrorResponse;
import com.microservices.orderservice.model.response.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {
    private static final String ERROR = "Error";

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<DefaultResponse> generalException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        DefaultResponse response = new DefaultResponse(ERROR, status.getReasonPhrase(), new Date(), status.value());
        return new ResponseEntity<>(response, new HttpHeaders(), status);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<DefaultResponse> handleDataNotFoundException(NotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        DefaultResponse response = new DefaultResponse(ERROR, e.getMessage(), new Date(), status.value());
        return new ResponseEntity<>(response, new HttpHeaders(), status);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<DefaultResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        DefaultResponse response = new DefaultResponse(ERROR, e.getMessage(), new Date(), status.value());
        return new ResponseEntity<>(response, new HttpHeaders(), status);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<DefaultResponse> handleUnauthorizedException(UnauthorizedException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        DefaultResponse response = new DefaultResponse(ERROR, ResponseMessage.UNAUTHORIZED, new Date(), status.value());
        return new ResponseEntity<>(response, new HttpHeaders(), status);
    }

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ErrorResponse<OutOfStockItemsDto>> handleOutOfStock(OutOfStockException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse<OutOfStockItemsDto> response = new ErrorResponse<>(ERROR, ex.getMessage(), new Date().toString(), status.value(), ex.getItems());
        return new ResponseEntity<>(response, new HttpHeaders(), status);
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    public ResponseEntity<DefaultResponse> handleDuplicateKeyException(DuplicateKeyException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        DefaultResponse response = new DefaultResponse(ERROR, e.getMessage(), new Date(), status.value());
        return new ResponseEntity<>(response, new HttpHeaders(), status);
    }
}