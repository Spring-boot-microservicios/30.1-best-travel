package com.angelfg.best_travel.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Configuration
public class SecurityConfig {

    private static final String[] PUBLIC_RESOURCES = {"/fly/**", "/hotel/**", "/swagger-ui/**", "/.well-known/**, ", "/v3/api-docs/**", "report/**"};
    private static final String[] USER_RESOURCES = {"/tour/**", "/ticket/**", "/reservation/**"};
    private static final String[] ADMIN_RESOURCES = {"/users/**"};
    private static final String LOGIN_RESOURCE = "/login";
    private static final String AUTH_WRITE = "write";
    private static final String AUTH_READ = "read";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";
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
    @Order(1) // Filtro de OAUTH
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
    @Order(2) // Filtro que valida en la aplicacion
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(Customizer.withDefaults())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(PUBLIC_RESOURCES).permitAll()
                .requestMatchers(USER_RESOURCES).hasAuthority(AUTH_READ)
                .requestMatchers(ADMIN_RESOURCES).hasAuthority(AUTH_WRITE)
            )
            .oauth2ResourceServer(oauth -> oauth
                .jwt(Customizer.withDefaults())
            )
            .formLogin(Customizer.withDefaults());

        return http.build();
    }

    // TODO: 16 - Filtro del usuario
    @Bean
    @Order(3) // Filtro de los usuarios por roles
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers(PUBLIC_RESOURCES).permitAll()
            .requestMatchers(USER_RESOURCES).hasRole(ROLE_USER)
            .requestMatchers(ADMIN_RESOURCES).hasRole(ROLE_ADMIN)
        );

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

    // TODO: 7 - Configuracion de ajustes de la autorizacion del servidor
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    // TODO: 8 - Configuracion para decodificar el jwk
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    // TODO: 9 - Generamos un RSA
    private static KeyPair generateRSA() {
        KeyPair keyPair = null;

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA"); // tipo de encriptacion
            keyPairGenerator.initialize(2048); // Tamaño de keyPair - estandar, o 4096
            keyPair = keyPairGenerator.generateKeyPair(); // Obtenemos la llave
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }

        return keyPair;
    }

    // TODO: 10 - Generamos una llave publica y una llave privada
    private static RSAKey generateKeys() {
        KeyPair keyPair = generateRSA();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey= (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    // TODO: 11 - Generamos la inyeccion de llaves en la configuracion del security
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateKeys();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    // TODO: 12 - Tiempo de vida del token 8 horas
    @Bean
    public TokenSettings tokenSettings() {
        return TokenSettings.builder()
            .refreshTokenTimeToLive(Duration.ofHours(8))
            .build();
    }

    // TODO: 13 - Quitar el prefijo de SCOPE_
    @Bean
    public JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        return converter;
    }

    // TODO: 14 - Quitar el prefijo de SCOPE_
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter) {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return converter;
    }

    // TODO: 15 - Añadir claims al token
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {

        return context -> {

            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) { // validar el access_token

                context.getClaims().claims(claim -> {
                    claim.putAll(Map.of(
                        "owner", APPLICATION_OWNER,
                        "date_request", LocalDateTime.now().toString()
                    ));
                });

            }

        };

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
