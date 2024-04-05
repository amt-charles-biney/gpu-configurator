package com.amalitech.gpuconfigurator.config;

import com.amalitech.gpuconfigurator.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/auth/**", "/api/v1/carts/**", "/api/v1/shipping/**").permitAll()
                .requestMatchers("api/v1/wishlists/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/webhook/easypost").permitAll()
                .requestMatchers("/api/v1/profile/**").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                .requestMatchers(HttpMethod.GET, "/api/v1/admin/orders/{id}").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                .requestMatchers("/api/v1/admin/**").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/cases/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/payment_info/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/product/**").permitAll()
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
                .permitAll()
                .requestMatchers("/api/v1/**").permitAll()
                .requestMatchers("/api/v1/admin/**").permitAll()
                .anyRequest()
                .authenticated());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
