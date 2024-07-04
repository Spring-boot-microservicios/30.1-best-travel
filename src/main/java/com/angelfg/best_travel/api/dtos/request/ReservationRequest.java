package com.angelfg.best_travel.api.dtos.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRequest implements Serializable {

    // valida 18 y 20 caracteres en strings
    @Size(min = 18, max = 20, message = "The size have to a length between 18 and 20 characters")
    @NotBlank(message = "Id client is mandatory") // no puede venir vacio ni null
    private String idClient;

    @Positive // Deben ser numeros positivos
    @NotNull(message = "Id hotel is mandatory")
    private Long idHotel;

    @Min(value =  1, message = "Min one days to make reservation")
    @Max(value = 30, message = "Max 30 days to make reservation")
    @NotNull(message = "total days is mandatory")
    private Integer totalDays;

    // @Pattern(regexp = "^(.+)@(.+)$") // expresion regular para verificar un email
    @Email(message = "Invalid email")
    private String email;
}
