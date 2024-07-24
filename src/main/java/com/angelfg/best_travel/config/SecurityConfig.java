package com.angelfg.best_travel.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
public class SecurityConfig {

    private static final String[] PUBLIC_RESOURCES = {"/fly/**", "/hotel/**", "/swagger-ui/**", "/.well-known/**, ", "/v3/api-docs/**", "report/**"};
    private static final String[] USER_RESOURCES = {"/tour/**", "/ticket/**", "/reservation/**"};
    private static final String[] ADMIN_RESOURCES = {"/users/**"};
    private static final String LOGIN_RESOURCE = "/login";
    private static final String ROLE_ADMIN = "write";
    private static final String APPLICATION_OWNER = "Debuggeando ideas";

    private final UserDetailsService userDetailsService;

    public SecurityConfig(@Qualifier(value = "appUserServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // TODO: 1 - Generamos el security filter
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http); // Configuracion default de cors

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class) // tipo de configuracion
                .oidc(Customizer.withDefaults());

        // Excepcion para redireccionar al login (estandar /login)
        http.exceptionHandling(e -> e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_RESOURCE)));

        return http.build();
    }

    // TODO: 4 - Proveedor de autenticacion
    @Bean
    public AuthenticationProvider authenticationProvider(BCryptPasswordEncoder encoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(encoder);
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
