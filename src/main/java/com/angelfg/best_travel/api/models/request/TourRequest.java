package com.angelfg.best_travel.api.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourRequest implements Serializable {

    // valida 18 y 20 caracteres en strings
    @Size(min = 18, max = 20, message = "The size have to a length between 18 and 20 characters")
    @NotBlank(message = "Id client is mandatory") // no puede venir vacio ni null
    public String customerId;

    // En las lista podemos poner que minimo tenga un objeto en el arreglo
    @Size(min = 1, message = "Min flight tour per tour")
    private Set<TourFlyRequest> flights;

    @Size(min = 1, message = "Min hotel tour per tour")
    private Set<TourHotelRequest> hotels;

    @Email(message = "Invalid email")
    private String email;

}
