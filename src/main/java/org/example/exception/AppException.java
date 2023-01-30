package org.example.exception;

import org.example.model.AppErrorCode;

public class AppException extends RuntimeException {
    public AppException(AppErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
