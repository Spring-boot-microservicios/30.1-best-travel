package com.angelfg.best_travel.api.dtos.request;

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
    public String customerId;
    private Set<TourFlyRequest> flights;
    private Set<TourHotelRequest> hotels;
}
