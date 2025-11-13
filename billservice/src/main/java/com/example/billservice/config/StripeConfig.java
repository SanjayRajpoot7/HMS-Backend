package com.example.billservice.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    // Injects the Stripe secret key from application properties
    @Value("${stripe.secret.key}")
    private String secretKey;

    // Sets the Stripe API key after the bean is constructed
    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }
}
