//package com.example.apigatewayservice.filter;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.cors.reactive.CorsUtils;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//@Configuration
//public class CorsGlobalFilter implements WebFilter, Ordered {
//
//    private static final String ALLOWED_HEADERS = "Authorization, Content-Type, Accept";
//    private static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS";
//    private static final String ALLOWED_ORIGIN = "http://localhost:4200";
//    private static final String EXPOSED_HEADERS = "Authorization";
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        if (CorsUtils.isCorsRequest(exchange.getRequest())) {
//            HttpHeaders headers = exchange.getResponse().getHeaders();
//            // Set CORS headers for the response
//            headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
//            headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
//            headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
//            headers.add("Access-Control-Allow-Credentials", "true");
//            headers.add("Access-Control-Expose-Headers", EXPOSED_HEADERS);
//
//            // If it's a preflight OPTIONS request, respond with OK directly
//            if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
//                exchange.getResponse().setStatusCode(HttpStatus.OK);
//                return Mono.empty();  // End request here for preflight
//            }
//        }
//
//        return chain.filter(exchange);  // Continue to next filter in chain
//    }
//
//    @Override
//    public int getOrder() {
//        return -1;  // Run before most other filters
//    }
//}






package com.example.apigatewayservice.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
public class CorsGlobalFilter implements WebFilter, Ordered {

    private static final String ALLOWED_HEADERS = "Authorization, Content-Type, Accept";
    private static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS";
    private static final String ALLOWED_ORIGIN = "http://localhost:4200";
    private static final String EXPOSED_HEADERS = "Authorization";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (CorsUtils.isCorsRequest(exchange.getRequest())) {
            HttpHeaders headers = exchange.getResponse().getHeaders();

            // Set headers (use set instead of add to avoid duplication)
            headers.set("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
            headers.set("Access-Control-Allow-Methods", ALLOWED_METHODS);
            headers.set("Access-Control-Allow-Headers", ALLOWED_HEADERS);
            headers.set("Access-Control-Allow-Credentials", "true");
            headers.set("Access-Control-Expose-Headers", EXPOSED_HEADERS);

            // Handle preflight request
            HttpMethod method = exchange.getRequest().getMethod();
            if (method != null && method == HttpMethod.OPTIONS) {
                exchange.getResponse().setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;  // High precedence
    }
}