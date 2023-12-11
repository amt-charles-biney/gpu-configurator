package com.amalitech.gpuconfigurator.config;

import com.amalitech.gpuconfigurator.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter extracts a JWT bearer token and performs checks to determine its validity.
 * This filter is used to configure security for authentication within the application.
 * It extends the OncePerRequestFilter class to ensure that it is only executed once per request.
 * It intercepts incoming requests, extracts the JWT token from the Authorization header, and validates
 * it against the configured security settings.
 *
 * The filter is responsible for authenticating and authorizing requests based on the provided JWT token.
 * If the token is valid, it sets the authentication details in the SecurityContextHolder to allow
 * further processing by the application.
 *
 * This filter is part of the Spring Security configuration and is typically applied to specific
 * endpoints or paths that require authentication.
 *
 * @author Clement Owireku-Bogyah
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * The JwtService responsible for JWT manipulation and validation.
     */
    public final JwtService jwtService;

    /**
     * The UserDetailsService used to load UserDetails based on the extracted user email from the JWT token.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Performs the authentication and authorization logic for incoming requests.
     *
     * @param request  The incoming HTTP request.
     * @param response The HTTP response.
     * @param filterChain The filter chain for the request.
     * @throws ServletException If an error occurs during the filter execution.
     * @throws IOException      If an I/O error occurs during the filter execution.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authenticationHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        // Check if the Authorization header is present and starts with "Bearer"
        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer")) {
            // Continue with the filter chain if the header is absent or not a valid Bearer token
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT token from the Authorization header
        jwtToken = authenticationHeader.substring(7);
        userEmail = jwtService.extractEmail(jwtToken);

        // Check if the user is not already authenticated and the extracted email is not null
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load UserDetails based on the extracted user email
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Validate the JWT token against the UserDetails
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                // Create an authentication token and set it in the SecurityContextHolder
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
