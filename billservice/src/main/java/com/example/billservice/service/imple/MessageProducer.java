package com.example.billservice.service.imple;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.billservice.config.RabbitMQConfig.EXCHANGE_NAME;
import static com.example.billservice.config.RabbitMQConfig.ROUTING_KEY;

@Service
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Sends a message to the configured RabbitMQ exchange with the specified routing key
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
        System.out.println("Sent message to RabbitMQ: " + message);
    }
}
