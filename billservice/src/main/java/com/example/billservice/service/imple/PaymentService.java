package com.example.billservice.service.imple;

import com.example.billservice.dto.BookingResponseDTO;
import com.example.billservice.entity.Bill;
import com.example.billservice.repository.BillRepository;
import com.example.billservice.service.BookingServiceClient;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private BillRepository billRepository;  // Repository for managing Bill entities in the database

    @Autowired
    private BookingServiceClient bookingServiceClient;  // Client to communicate with Booking Service to fetch/update bookings

    @Autowired
    EmailSenderService emailSenderService;  // Service used to send emails (not used in this snippet but declared)

    @Autowired
    MessageProducer messageProducer;  // Service to send messages/events (like to RabbitMQ)

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    public Session createCheckoutSession(@NotNull Double amount, String currency, String customerName, String email) throws Exception {
        // Build the checkout session parameters
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)  // Set the mode to payment
                .setSuccessUrl("http://localhost:8085/api/payment/success?sessionId={CHECKOUT_SESSION_ID}")  // URL to redirect on success
                .setCancelUrl("http://localhost:8085/api/payment/failed?sessionId={CHECKOUT_SESSION_ID}")   // URL to redirect on cancellation
                .setCustomerEmail(email)  // Pre-fill customer email in Stripe checkout
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)  // Quantity set to 1
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(currency)  // Currency for payment
                                                .setUnitAmount((long) (amount * 100))  // Amount in smallest currency unit (e.g. cents, paise)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(customerName)  // Use customerName as product name
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .setCustomerCreation(SessionCreateParams.CustomerCreation.ALWAYS)  // Always create a Stripe customer
                .setShippingAddressCollection(
                        SessionCreateParams.ShippingAddressCollection.builder()
                                .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.IN)  // Only allow shipping to India
                                .build()
                )
                .build();

        // Create the session using Stripe API
        Session session = Session.create(params);

        // Log session details for debugging
        log.info("Created Stripe Session: {}", session);
        log.info("Created Stripe Session ID: {}", session.getId());
        log.info("Created Stripe Payment URL: {}", session.getUrl());

        return session;
    }


    public Session retrieveSession(String sessionId) throws Exception {
        Session session = Session.retrieve(sessionId);  // Fetch session details from Stripe
        log.info(String.valueOf(session));  // Log session info
        return session;
    }

    public ResponseEntity<String> handlePaymentSuccess(String sessionId) {
        try {
            // 1. Retrieve the Stripe checkout session to verify payment details
            Session session = retrieveSession(sessionId);

            // 2. Check the payment status from Stripe session
            if ("paid".equalsIgnoreCase(session.getPaymentStatus())) {

                // 3. Find the bill associated with this Stripe session
                Optional<Bill> billOptional  = billRepository.findBySessionId(sessionId);

                if (billOptional.isPresent()) {
                    Bill bill = billOptional.get();

                    // 4. Update bill payment status and payment date/time
                    bill.setPaymentStatus("SUCCESS");
                    bill.setPaymentDate(LocalDateTime.now());
                    billRepository.save(bill);  // Persist changes to the database

                    // 5. Fetch booking details from Booking Service using bookingId from bill
                    BookingResponseDTO bookingDetails = bookingServiceClient.getBooking(bill.getBookingId());

                    // 6. Prepare a custom message with booking and payment details for event publishing
                    String customMessage = bookingDetails.getGuest().getEmail() + "|" + bill.getBookingId() + "|" +
                            bookingDetails.getGuest().getFullName() + "|" +
                            bookingDetails.getRoom().getRoomType() + "|" +
                            bookingDetails.getNumGuests() + "|" +
                            bookingDetails.getCheckinDate() + "|" +
                            bookingDetails.getCheckoutDate() + "|" +
                            bill.getTotalAmount() + " " + session.getCurrency();

                    // 7. Send/publish the custom message to other systems/services (e.g., for email notification)
                    messageProducer.sendMessage(customMessage);

                    // 8. Update the booking status to reflect payment success
//                    bookingServiceClient.updateBookingStatus(bill.getBookingId());

                    bookingServiceClient.updateBookingStatus(bill.getBookingId(), "CONFIRMED");


                    return ResponseEntity.ok("Payment successful. Bill status updated.");

                } else {
                    // Bill not found for the given sessionId
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Bill not found for session ID: " + sessionId);
                }
            } else {
                // Payment was not completed successfully
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Payment not completed for session ID: " + sessionId);
            }

        } catch (Exception e) {
            // Handle any exceptions and return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing payment success: " + e.getMessage());
        }
    }
}
