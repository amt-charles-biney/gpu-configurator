package com.amalitech.gpuconfigurator.config;

import com.amalitech.gpuconfigurator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {


    private final UserRepository repository;

    /**
     * Creates a UserDetailsService bean that retrieves user details from the UserRepository.
     *
     * @return An implementation of UserDetailsService.
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> repository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("No Username found"));
    }

    /**
     * Creates an AuthenticationProvider bean using DaoAuthenticationProvider.
     * It sets the UserDetailsService and PasswordEncoder for authentication.
     *
     * @return An implementation of AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Creates an AuthenticationManager bean using AuthenticationConfiguration.
     *
     * @param config The AuthenticationConfiguration used to create the AuthenticationManager.
     * @return An implementation of AuthenticationManager.
     * @throws Exception If an error occurs while creating the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Creates a PasswordEncoder bean for secure password hashing.
     *
     * @return An implementation of PasswordEncoder (BCryptPasswordEncoder).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
