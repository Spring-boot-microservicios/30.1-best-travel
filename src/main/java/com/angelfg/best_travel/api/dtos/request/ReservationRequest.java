package com.angelfg.best_travel.api.dtos.request;

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
    private String idClient;
    private Long idHotel;
    private Integer totalDays;
}
