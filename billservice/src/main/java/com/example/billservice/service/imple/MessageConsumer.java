package com.example.billservice.service.imple;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import static com.example.billservice.config.RabbitMQConfig.QUEUE_NAME;

@Component
public class MessageConsumer {

    private final EmailSenderService emailService;

    // Constructor Injection
    public MessageConsumer(EmailSenderService emailService) {
        this.emailService = emailService;
    }

    // Listens to RabbitMQ queue for incoming messages
    @RabbitListener(queues = QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Received message from RabbitMQ: " + message);
        try {
            String[] data = message.split("\\|");
            if (data.length != 8) {
                System.err.println("Invalid message format: " + message);
                return;
            }

            for (int i = 0; i < data.length; i++) {
                data[i] = data[i].trim();
            }

            String email = data[0];
            String bookingId = data[1];
            String guestName = data[2];
            String roomType = data[3];
            String numGuests = data[4];
            String checkinDate = data[5];
            String checkoutDate = data[6];
            String totalAmount = data[7];

            String subject = "Payment Successful - Booking Confirmation";
            String body = "Dear " + guestName + ",\n\n" +
                    "We are pleased to inform you that your payment for Booking ID: " + bookingId + " has been successfully processed.\n\n" +
                    "Booking Details:\n" +
                    "Room Type: " + roomType + "\n" +
                    "Number of Guests: " + numGuests + "\n" +
                    "Check-in Date: " + checkinDate + "\n" +
                    "Check-out Date: " + checkoutDate + "\n" +
                    "Total Amount: " + totalAmount + "\n\n" +
                    "Your reservation is confirmed! Thank you for choosing our service.\n\n" +
                    "Best regards,\nYour Booking Team";

            sendEmailAndLog(email, subject, body);

        } catch (Exception ex) {
            System.err.println("Error processing message: " + message);
            ex.printStackTrace();
        }
    }

    // Utility method to send email and print confirmation to console
    private void sendEmailAndLog(String email, String subject, String body) {
        emailService.sendEmail(email, subject, body);
        System.out.println("Email sent to: " + email + " | Subject: " + subject);
    }
}
