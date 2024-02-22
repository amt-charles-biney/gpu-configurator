package com.amalitech.gpuconfigurator.config;

import com.amalitech.gpuconfigurator.interceptors.UserSessionInterceptor;
import com.amalitech.gpuconfigurator.repository.UserSessionRepository;
import com.amalitech.gpuconfigurator.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {
    private final UserSessionRepository userSessionRepository;

    private final JwtService jwtService;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new UserSessionInterceptor(userSessionRepository, jwtService))
                .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**");
    }
}