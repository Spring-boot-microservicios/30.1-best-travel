package com.angelfg.best_travel.infraestructure.mongoServices;

import com.angelfg.best_travel.domain.repositories.mongo.AppUserRepository;
import com.angelfg.best_travel.infraestructure.abstract_mongo_services.ModifyUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class AppUserServiceImpl implements ModifyUserService {

    private final AppUserRepository appUserRepository;

    @Override
    public Map<String, Boolean> enabled(String username) {
        return Map.of();
    }

    @Override
    public Map<String, List<String>> addRole(String username, String role) {
        return Map.of();
    }

    @Override
    public Map<String, List<String>> removeRole(String username, String role) {
        return Map.of();
    }

}
