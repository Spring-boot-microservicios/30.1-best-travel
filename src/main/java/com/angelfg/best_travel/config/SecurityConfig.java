package com.angelfg.best_travel.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.UUID;

@Configuration
public class SecurityConfig {

    private static final String[] PUBLIC_RESOURCES = {"/fly/**", "/hotel/**", "/swagger-ui/**", "/.well-known/**, ", "/v3/api-docs/**", "report/**"};
    private static final String[] USER_RESOURCES = {"/tour/**", "/ticket/**", "/reservation/**"};
    private static final String[] ADMIN_RESOURCES = {"/users/**"};
    private static final String LOGIN_RESOURCE = "/login";
    private static final String ROLE_ADMIN = "write";
    private static final String APPLICATION_OWNER = "AngelDeveloper";

    @Value("${app.client.id}")
    private String clientId;
    @Value("${app.client.secret}")
    private String clientSecret;
    @Value("${app.client-scope-read}")
    private String scopeRead;
    @Value("${app.client-scope-write}")
    private String scopeWrite;
    @Value("${app.client-redirect-debugger}")
    private String redirectUri1;
    @Value("${app.client-redirect-spring-doc}")
    private String redirectUri2;

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

    // TODO: 5 - Filtro de rutas publicas, privadas y con accesos
    @Bean
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(Customizer.withDefaults())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(PUBLIC_RESOURCES).permitAll()
                .requestMatchers(USER_RESOURCES).authenticated()
                .requestMatchers(ADMIN_RESOURCES).hasRole(ROLE_ADMIN)
            )
            .oauth2ResourceServer(oauth -> oauth
                .jwt(Customizer.withDefaults())
            )
            .formLogin(Customizer.withDefaults());

        return http.build();
    }

    // TODO: 6 - Doble autenticacion, configuracion via aplicacion back
    @Bean
    public RegisteredClientRepository registeredClientRepository(BCryptPasswordEncoder encoder) {

        // Registra al cliente
        RegisteredClient registeredClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(encoder.encode(clientSecret))
                .scope(scopeRead)
                .scope(scopeWrite)
                .redirectUri(redirectUri1) // debugger
                .redirectUri(redirectUri2) // spring-doc
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // tipo de autenticacion basic
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) // tipo de autenticacion
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient); // En memoria
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
