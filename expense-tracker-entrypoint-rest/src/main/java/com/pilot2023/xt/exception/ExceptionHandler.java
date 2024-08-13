package com.pilot2023.xt.exception;

import com.pilot2023.xt.mapper.ExpenseMapperEntryPointRest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandler {

    private final ExpenseMapperEntryPointRest mapper;

    @org.springframework.web.bind.annotation.ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionDto> handleBusinessException(BusinessException e, WebRequest request) {
        ExceptionDto exception = mapper.toExceptionDto(e);
        return new ResponseEntity<>(
                exception,
                new HttpHeaders(),
                HttpStatus.valueOf(exception.getStatus())
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleGenericException(Exception e, WebRequest request) {
        BusinessException businessException = new BusinessException(ExceptionCode.INTERNAL_SERVER_ERROR, e.getMessage());
        return handleBusinessException(businessException, request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        String msg = Optional.ofNullable(e.getFieldError())
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse(e.getMessage());
        BusinessException businessException = new BusinessException(ExceptionCode.PARAMETER_VALIDATION_ERROR, msg);
        return handleBusinessException(businessException, request);
    }


}