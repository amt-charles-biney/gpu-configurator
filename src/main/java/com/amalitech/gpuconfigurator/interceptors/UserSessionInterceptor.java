package com.amalitech.gpuconfigurator.interceptors;

import com.amalitech.gpuconfigurator.model.Cart;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.repository.UserSessionRepository;
import com.amalitech.gpuconfigurator.service.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class UserSessionInterceptor implements HandlerInterceptor {
    private final String USER_SESSION_COOKIE_NAME = "user_session";

    private final int USER_SESSION_COOKIE_MAX_AGE = 365 * 24 * 60 * 60; // 1yr

    private final UserSessionRepository userSessionRepository;

    private final JwtService jwtService;

    public UserSessionInterceptor(UserSessionRepository userSessionRepository, JwtService jwtService) {
        this.userSessionRepository = userSessionRepository;
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:4200");

        if (request.getCookies() == null) {
            createNewUserSessionCookie(request, response);
            return true;
        }

        Optional<Cookie> userSessionCookieOptional = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(USER_SESSION_COOKIE_NAME))
                .findFirst();

        if (userSessionCookieOptional.isEmpty()) {
            createNewUserSessionCookie(request, response);
            return true;
        }

        Cookie userSessionCookie = userSessionCookieOptional.get();
        String userSessionId = jwtService.extractSubject(userSessionCookie.getValue());
        Optional<UserSession> userSessionOptional = userSessionRepository.findById(UUID.fromString(userSessionId));

        if (userSessionOptional.isEmpty()) {
            createNewUserSessionCookie(request, response);
            return true;
        }

        request.setAttribute("userSession", userSessionOptional.get());

        return true;
    }

    private void createNewUserSessionCookie(HttpServletRequest request, HttpServletResponse response) {
        UserSession userSession = createUserSession(request);
        Cookie newSessionCookie = createUserSessionCookie(userSession);
        response.addCookie(newSessionCookie);
        request.setAttribute("userSession", userSession);
    }

    private Cookie createUserSessionCookie(UserSession userSession) {
        String cookieJwt = jwtService.generateTokenForUserSession(userSession);

        Cookie sessionCookie = new Cookie(USER_SESSION_COOKIE_NAME, cookieJwt);

        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(USER_SESSION_COOKIE_MAX_AGE);
        sessionCookie.setHttpOnly(true);

        return sessionCookie;
    }

    private UserSession createUserSession(HttpServletRequest request) {
        UserSession userSession = new UserSession();

        Optional<String> userAgent = getUserAgent(request);
        if (userAgent.isPresent() && !userAgent.get().isBlank()) {
            userSession.setUserAgent(userAgent.get());
        }

        Optional<String> ipAddress = getIpAddress(request);
        if (ipAddress.isPresent() && !ipAddress.get().isBlank()) {
            userSession.setIpAddress(ipAddress.get());
        }

        userSession.setCart(new Cart());

        return userSessionRepository.save(userSession);
    }

    private Optional<String> getUserAgent(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("User-Agent"));
    }

    private Optional<String> getIpAddress(HttpServletRequest request) {
        String ipFromHeader = request.getHeader("X-Forwarded-For");
        if (ipFromHeader != null && !ipFromHeader.isEmpty()) {
            return Optional.of(ipFromHeader);
        }
        return Optional.ofNullable(request.getRemoteAddr());
    }
}
