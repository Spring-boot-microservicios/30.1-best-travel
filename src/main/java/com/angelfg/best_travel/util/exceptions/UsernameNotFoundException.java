package com.angelfg.best_travel.util.exceptions;

public class UsernameNotFoundException extends RuntimeException {

    public static final String ERROR_MESSAGE = "User not exist in %s";

    public UsernameNotFoundException(String tableName) {
        super(String.format(ERROR_MESSAGE, tableName));
    }

}
