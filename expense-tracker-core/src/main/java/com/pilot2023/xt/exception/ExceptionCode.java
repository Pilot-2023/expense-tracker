package com.pilot2023.xt.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@RequiredArgsConstructor
public enum ExceptionCode {
    INTERNAL_SERVER_ERROR("EXPENSE-001", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    NOT_FOUND("EXPENSE-002", HttpStatus.NOT_FOUND.value()),
    ALREADY_EXISTS("EXPENSE-003", HttpStatus.CONFLICT.value()),
    PARAMETER_VALIDATION_ERROR("EXPENSE-004", HttpStatus.BAD_REQUEST.value());

    private final String code;
    private final int httpStatusCode;

}