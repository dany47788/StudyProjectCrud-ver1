package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppStatusCode {

    SQL_EXCEPTION(1, "Sql error"),
    IS_NULL_EXCEPTION(2, "Argument is NULL"),
    NOT_FOUND_EXCEPTION(3, "Not found");

    private final Integer code;
    private final String message;
}
