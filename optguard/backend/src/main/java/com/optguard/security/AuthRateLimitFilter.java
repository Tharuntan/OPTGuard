package com.optguard.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthRateLimitFilter extends OncePerRequestFilter {
    private static final int MAX_REQUESTS = 20;
    private static final Duration WINDOW = Duration.ofMinutes(15);
    private static final int CLEANUP_THRESHOLD = 5_000;

    private final ConcurrentHashMap<String, RateState> attempts = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public AuthRateLimitFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (!isLimitedRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        Instant now = Instant.now();
        String key = clientKey(request);
        RateState state = attempts.compute(key, (ignored, current) -> {
            if (current == null || !current.resetAt().isAfter(now)) {
                return new RateState(1, now.plus(WINDOW));
            }
            return new RateState(current.count() + 1, current.resetAt());
        });

        if (attempts.size() > CLEANUP_THRESHOLD) {
            cleanupExpired(now);
        }

        if (state.count() > MAX_REQUESTS) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), Map.of(
                    "status", HttpStatus.TOO_MANY_REQUESTS.value(),
                    "error", "Too Many Requests",
                    "message", "Too many sign-in attempts. Please wait and try again."
            ));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isLimitedRequest(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        String path = request.getRequestURI();
        return "/api/auth/login".equals(path) || "/api/auth/register".equals(path);
    }

    private String clientKey(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        String ip = forwardedFor == null || forwardedFor.isBlank()
                ? request.getRemoteAddr()
                : forwardedFor.split(",")[0].trim();
        return ip + ":" + request.getRequestURI();
    }

    private void cleanupExpired(Instant now) {
        attempts.entrySet().removeIf(entry -> !entry.getValue().resetAt().isAfter(now));
    }

    private record RateState(int count, Instant resetAt) {
    }
}
