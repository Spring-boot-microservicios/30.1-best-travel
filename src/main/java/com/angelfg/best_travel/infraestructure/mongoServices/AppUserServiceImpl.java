package com.angelfg.best_travel.infraestructure.mongoServices;

import com.angelfg.best_travel.domain.entities.documents.AppUserDocument;
import com.angelfg.best_travel.domain.repositories.mongo.AppUserRepository;
import com.angelfg.best_travel.infraestructure.abstract_mongo_services.ModifyUserService;
import com.angelfg.best_travel.util.exceptions.UsernameNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AppUserServiceImpl implements ModifyUserService {

    private static final String COLLECTION_NAME = "app_user";

    private final AppUserRepository appUserRepository;

    @Override
    public Map<String, Boolean> enabled(String username) {
        AppUserDocument user = this.appUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(COLLECTION_NAME));
        user.setEnabled(!user.isEnabled());

        AppUserDocument userSaved = this.appUserRepository.save(user);
        return Collections.singletonMap(userSaved.getUsername(), userSaved.isEnabled());
    }

    @Override
    public Map<String, Set<String>> addRole(String username, String role) {
        AppUserDocument user = this.appUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(COLLECTION_NAME));
        user.getRole().getGrantedAuthorities().add(role);

        AppUserDocument userSaved = this.appUserRepository.save(user);
        Set<String> authorities = userSaved.getRole().getGrantedAuthorities();

        log.info("User {} add role {}", userSaved.getUsername(), userSaved.getRole().getGrantedAuthorities().toString());

        return Collections.singletonMap(userSaved.getUsername(), authorities);
    }

    @Override
    public Map<String, Set<String>> removeRole(String username, String role) {
        AppUserDocument user = this.appUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(COLLECTION_NAME));
        user.getRole().getGrantedAuthorities().remove(role);

        AppUserDocument userSaved = this.appUserRepository.save(user);
        Set<String> authorities = userSaved.getRole().getGrantedAuthorities();

        log.info("User {} remove role {}", userSaved.getUsername(), userSaved.getRole().getGrantedAuthorities().toString());

        return Collections.singletonMap(userSaved.getUsername(), authorities);
    }

    @Transactional(readOnly = true)
    private void loadUserByUsername(String username) {
        AppUserDocument user = this.appUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(COLLECTION_NAME));

    }

}
