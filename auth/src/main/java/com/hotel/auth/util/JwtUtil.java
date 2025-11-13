package com.hotel.auth.util;

import com.hotel.auth.model.User;
import com.hotel.auth.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Autowired
    private UserRepository userRepository;


    @Value("${jwt.secret}")
    private String secret = "A%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C&F)A%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C&F)A%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C&F)A%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C&F)";
    private long expiration = 604800000; //  day

    public String generateToken(String username) {
        logger.info("Generating token for username: {}", username);
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (!userOptional.isPresent()) {
            logger.error("User not found for username: {}", username);
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();
        String role = user.getRole();

//        System.out.println("-------------JWT UTIL------- AUTH");
        // Validate role
        if (!"ADMIN".equals(role) && !"RECEPTIONIST".equals(role) && !"MANAGER".equals(role)) {
            logger.error("Invalid user role: {}", role);
            throw new RuntimeException("Invalid user role");
        }

        System.out.println("Issue AT " + new Date());
        System.out.println("Exprestion at " + new Date(System.currentTimeMillis() + expiration));
        String token = Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
        System.out.println(token);
        logger.info("Token generated successfully for username: {}", username);
        return token;
    }

}