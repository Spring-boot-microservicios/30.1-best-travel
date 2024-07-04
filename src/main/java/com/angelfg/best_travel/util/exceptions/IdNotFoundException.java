package com.angelfg.best_travel.util.exceptions;

public class IdNotFoundException extends RuntimeException {

    public static final String ERROR_MESSAGE = "Record not exist in %s";

    public IdNotFoundException(String tableName) {
        super(String.format(ERROR_MESSAGE, tableName));
    }

}
