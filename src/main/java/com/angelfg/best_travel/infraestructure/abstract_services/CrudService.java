package com.angelfg.best_travel.infraestructure.abstract_services;

/**
 * Interface generica para un CRUD
 * @param <RQ> - Request
 * @param <RS> - Response
 * @param <ID> - id (Long, UUID, etc)
 */
public interface CrudService <RQ, RS, ID> {
    RS create(RQ request);
    RS read(ID id);
    RS update(RQ request, ID id);
    void delete(ID id);
}
