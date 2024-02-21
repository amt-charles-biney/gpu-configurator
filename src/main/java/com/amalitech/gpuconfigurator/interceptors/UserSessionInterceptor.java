package com.amalitech.gpuconfigurator.interceptors;

import com.amalitech.gpuconfigurator.repository.UserSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserSessionInterceptor implements HandlerInterceptor {
    private final UserSessionRepository userSessionRepository;

    public UserSessionInterceptor(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }
}
