package com.angelfg.best_travel.infraestructure.abstract_services;

import com.angelfg.best_travel.api.models.response.HotelResponse;

import java.util.Set;

public interface HotelService extends CatalogService<HotelResponse> {
    Set<HotelResponse> readByRaiting(Integer raiting);
}
