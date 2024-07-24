package com.angelfg.best_travel.domain.repositories.mongo;

import com.angelfg.best_travel.domain.entities.documents.AppUserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppUserRepository extends MongoRepository<AppUserDocument, String> {

}
