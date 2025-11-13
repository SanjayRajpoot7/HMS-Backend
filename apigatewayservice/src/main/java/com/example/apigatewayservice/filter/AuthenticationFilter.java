package com.example.apigatewayservice.filter;

import com.example.apigatewayservice.exception.ForbiddenAccessException;
import com.example.apigatewayservice.exception.UnauthorizedUserException;
import com.example.apigatewayservice.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private final List<String> openApiEndpoints = List.of(
            "/api/auth/**",       // now skips register & login
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    private final Map<String, List<String>> routeRoleMap = Map.ofEntries(
            Map.entry("/api/auth/**",                 List.of("ALL")),
            Map.entry("/api/rooms/**",                List.of("ADMIN","MANAGER")),
            Map.entry("/api/rooms/available/**", List.of("ADMIN", "RECEPTIONIST", "MANAGER")),
            Map.entry("/api/rooms/getAll",         List.of("RECEPTIONIST")),
            Map.entry("/api/rooms/available/all",         List.of("ADMIN", "RECEPTIONIST", "MANAGER")),
            Map.entry("/api/bookings/available/**",   List.of("ADMIN", "RECEPTIONIST")),
            Map.entry("/api/inventory/**",            List.of("ADMIN", "MANAGER")),
            Map.entry("/api/bookings/**",             List.of("ADMIN", "RECEPTIONIST")),
            Map.entry("/api/bills/**",                List.of("ADMIN", "RECEPTIONIST")),
            Map.entry("/api/staff/**",                List.of("ADMIN", "MANAGER")),
            Map.entry("/api/payment/**",              List.of("ADMIN", "RECEPTIONIST")),
            Map.entry("/api/guests/**",               List.of("ADMIN", "RECEPTIONIST"))
    );
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        logger.info("Incoming request path: {}", path);


        if (isOpenApi(path)) {
            logger.debug("Open API path detected: {}", path);
            return chain.filter(exchange);
        }

        List<String> authHeaders = exchange.getRequest().getHeaders().getOrEmpty("Authorization");



        if (authHeaders.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeaders.get(0).replace("Bearer ", "").trim();

        try {
            boolean valid = jwtUtil.validateToken(token);
            String role = jwtUtil.extractRole(token);
            System.out.println(role);
            logger.info("Token validated. Role extracted: {}", role);


            if (!valid) {
                throw new UnauthorizedUserException("Invalid or expired token");
            }

            if (!isAuthorized(path, role)) {
                logger.warn("Access denied. Role '{}' not allowed for path '{}'", role, path);
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            logger.debug("Access granted for role '{}' on path '{}'", role, path);
        }
        catch (Exception e) {
            logger.error("Token validation failed for path '{}': {}", path, e.getMessage());
            throw new UnauthorizedUserException("Invalid or expired token");
        }finally {
            System.out.println("Authorization process completed.");
        }

        return chain.filter(exchange);
    }

    private boolean isOpenApi(String path) {
        return openApiEndpoints.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private boolean isAuthorized(String path, String role) {
        for (var entry : routeRoleMap.entrySet()) {
            String pattern = entry.getKey();
            List<String> allowed = entry.getValue();
            if (pathMatcher.match(pattern, path)) {
                return allowed.contains("ALL") || allowed.contains(role.toUpperCase());
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
