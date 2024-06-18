package com.angelfg.best_travel.api.dtos.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelResponse implements Serializable {
    private Long id;
    private String name;
    private String address;
    private Integer rating;
    private BigDecimal price;
}
