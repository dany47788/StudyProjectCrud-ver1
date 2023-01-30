package org.example.exception;

import org.example.model.AppErrorCode;

public class NotFoundException extends RuntimeException {
    public NotFoundException(AppErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
