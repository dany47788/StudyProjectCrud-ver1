package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum AppErrorCode {

    SQL_EXCEPTION(1, "Sql error");

    private final Integer code;
    private final String message;
}
