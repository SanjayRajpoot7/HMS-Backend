package com.example.apigatewayservice.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key key;

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C&F)A%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C&F)A%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C&F)A%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C&F".getBytes());

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        logger.info("JWT key initialized with secret.");
    }

    public String generateToken(String username) {
        logger.debug("Generating token for user: {}", username);
        String token = Jwts.builder()
                .setSubject(username)
                .claim("role", "USER")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        logger.debug("Token generated for user: {}", username);
        return token;
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token.replace("Bearer ",""));
            System.out.println("Token: [" + token + "]");
            logger.debug("Token is valid");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public String extractRole(String token) {
        logger.debug("Extracting role from token");
        Claims claims = (Claims) Jwts.parser().verifyWith(SECRET_KEY).build().parse(token.replace("Bearer ","")).getPayload();

        return claims.get("role", String.class);
    }
}
