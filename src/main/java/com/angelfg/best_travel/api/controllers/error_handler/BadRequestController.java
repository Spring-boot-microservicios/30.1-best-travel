package com.angelfg.best_travel.api.controllers.error_handler;

import com.angelfg.best_travel.api.models.response.BaseErrorResponse;
import com.angelfg.best_travel.api.models.response.ErrorResponse;
import com.angelfg.best_travel.api.models.response.ErrorsResponse;
import com.angelfg.best_travel.util.exceptions.IdNotFoundException;
import com.angelfg.best_travel.util.exceptions.UsernameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

// 400, 401, ..
@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestController {

    @ExceptionHandler({ IdNotFoundException.class, UsernameNotFoundException.class })
    public BaseErrorResponse handleIdNotFound(RuntimeException exception) { // Uso de polimorfismo

        return ErrorResponse.builder()
                .error(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<>();
        exception.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));

        return ErrorsResponse.builder()
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }

}
