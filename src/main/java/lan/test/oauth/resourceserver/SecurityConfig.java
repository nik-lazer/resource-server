package lan.test.oauth.resourceserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final static Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    @Autowired
    private OAuth2ResourceServerProperties properties;
    private final static String[] AUTH_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/configuration/security",
            "/swagger-resources/configuration/ui",
            "/swagger-ui/**",
            "/index.html",
            "/",
            "/actuator/health/liveness",
            "/actuator/health/readiness"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        http
                .authorizeHttpRequests(it->it.requestMatchers(AUTH_WHITELIST).permitAll())
                .authorizeHttpRequests(it->it.anyRequest().authenticated())
                .oauth2ResourceServer(it->it.jwt(jwt->jwt.decoder(jwtDecoder))
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.warn("Fail to log in with header {} and error {}", request.getHeader("Authorization"), authException.getMessage(), authException);
                            var delegate = new BearerTokenAuthenticationEntryPoint();
                            delegate.commence(request, response, authException);
                        }));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(properties.getJwt().getIssuerUri());
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withIssuer);
        var jwtDecoder = NimbusJwtDecoder.withJwkSetUri(properties.getJwt().getJwkSetUri()).build();
        jwtDecoder.setJwtValidator(validator);
        return jwtDecoder;
    }
}