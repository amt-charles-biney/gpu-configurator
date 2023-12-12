package com.amalitech.gpuconfigurator.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configuration class for defining the security filter chain.
     * This class configures security-related settings for HTTP requests,
     * including CSRF protection, URL authorization, session management,
     * authentication providers, and filters.
     *
     * It defines a custom SecurityFilterChain bean that specifies the
     * security configuration for specific URL patterns.
     *
     * @param http The HttpSecurity object to configure security settings.
     * @return A SecurityFilterChain that represents the configured security.
     * @throws Exception If an error occurs during configuration.
     *
     * @author ClementOwireku-Bogyah
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth-> auth.requestMatchers("/api/v1/auth/**").permitAll().anyRequest().authenticated())
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authenticationProvider)
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
    }
}
