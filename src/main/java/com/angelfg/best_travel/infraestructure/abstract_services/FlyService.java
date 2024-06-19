package com.angelfg.best_travel.infraestructure.abstract_services;

import com.angelfg.best_travel.api.dtos.response.FlyResponse;

import java.util.Set;

public interface FlyService extends CatalogService<FlyResponse> {
    Set<FlyResponse> readByOriginDestiny(String origen, String destiny);
}
