package com.angelfg.best_travel.api.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder // clase padre
public class BaseErrorResponse implements Serializable {
    private String status;
    private Integer code;
}
