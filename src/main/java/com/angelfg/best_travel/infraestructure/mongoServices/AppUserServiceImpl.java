package com.angelfg.best_travel.infraestructure.mongoServices;

import com.angelfg.best_travel.domain.entities.documents.AppUserDocument;
import com.angelfg.best_travel.domain.repositories.mongo.AppUserRepository;
import com.angelfg.best_travel.infraestructure.abstract_mongo_services.ModifyUserService;
import com.angelfg.best_travel.util.exceptions.UsernameNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service // ("appUserServiceImpl")
@AllArgsConstructor
@Slf4j
@Transactional
public class AppUserServiceImpl implements ModifyUserService, UserDetailsService {

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

    // TODO: 2 - Configuracion del filtro loadUserByUsername
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) {
        AppUserDocument user = this.appUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(COLLECTION_NAME));
        return mapUserToUserDetails(user);
    }

    // TODO: 3 - Generar un UserDetails apartir de nuestro usuario de DB
    private static UserDetails mapUserToUserDetails(AppUserDocument user) {
        Set<GrantedAuthority> authorities = user.getRole().getGrantedAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new User(
            user.getUsername(),
            user.getPassword(),
            user.isEnabled(),
            true, // no esta expirada su cuenta
            true, // credenciales no expiradas
            true, // cuenta no bloqueada
            authorities
        );
    }

}
