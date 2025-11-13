package com.example.billservice.service.imple;

import com.example.billservice.dto.BillResponseDTO;
import com.example.billservice.dto.BookingResponseDTO;
import com.example.billservice.dto.PaymentResponse;
import com.example.billservice.entity.Bill;
import com.example.billservice.exception.BillAlreadyPaidException;
import com.example.billservice.exception.ResourceNotFoundException;
import com.example.billservice.repository.BillRepository;
import com.example.billservice.service.BillService;
import com.example.billservice.service.BookingServiceClient;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BookingServiceClient bookingServiceClient;

    @Override
    public PaymentResponse createBill(Long bookingId) throws Exception {

        log.info("Create bill request received for bookingId: {}", bookingId);

        // Get booking details from Booking Service
        BookingResponseDTO booking = bookingServiceClient.getBooking(bookingId);
        log.debug("Booking details received: {}", booking);

        // Check if a bill already exists for this booking
        Optional<Bill> existingBill = billRepository.findByBookingId(bookingId);

        if (existingBill.isPresent()) {
            Bill bill = existingBill.get();
            log.info("Existing bill found with ID: {}", bill.getId());

            // If bill is already paid, throw exception
            if (bill.getPaymentStatus().equalsIgnoreCase("SUCCESS")) {
                log.warn("Bill is already paid for booking ID: {}", bookingId);
                throw new BillAlreadyPaidException("Bill is already paid for booking ID: " + bookingId);
            }

            // If payment session exists, validate or return existing session info
            if (bill.getSessionId() != null) {
                log.info("Returning existing payment session for booking ID: {}", bookingId);

                Session existingSession = paymentService.retrieveSession(bill.getSessionId());
                log.debug("Existing payment session status: {}", existingSession.getStatus());

                // If existing session is expired or canceled, mark bill as FAILED
                if ("expired".equalsIgnoreCase(existingSession.getStatus()) ||
                        "canceled".equalsIgnoreCase(existingSession.getStatus())) {
                    bill.setPaymentStatus("FAILED");
                    billRepository.save(bill);
                    log.warn("Payment session expired or canceled for booking ID: {}", bookingId);
                    throw new Exception("Payment session expired. Please create a new payment.");
                }

                // Create new session and return response
                Session session = paymentService.createCheckoutSession(
                        bill.getTotalAmount(), "INR", bill.getCustomerName(), booking.getGuest().getEmail());

                PaymentResponse paymentResponse = new PaymentResponse();
                paymentResponse.setCustomerName(bill.getCustomerName());
                paymentResponse.setSessionId(bill.getSessionId());
                paymentResponse.setSessionUrl(session.getUrl());
                log.info("Returning existing payment session response for booking ID: {}", bookingId);
                return paymentResponse;
            }
        }

        LocalDate checkInDate = booking.getCheckinDate();
        LocalDate checkOutDate = booking.getCheckoutDate();
//        int totalDays = checkOutDate.getDayOfYear() - checkInDate.getDayOfYear();
        long totalDays = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        log.debug("Calculated total days: {}", totalDays);

        // Create new bill if not present
        Bill bill = new Bill();
        bill.setBookingId(booking.getBookingId());
        bill.setCustomerName(booking.getGuest().getFullName());
        bill.setTotalAmount(booking.getRoom().getPricePerNight() * totalDays);
        bill.setPaymentMode("CARD");
        bill.setPaymentStatus("PENDING");
        bill.setPaymentDate(LocalDateTime.now());

        // Create Stripe checkout session
        Session session = paymentService.createCheckoutSession(
                bill.getTotalAmount(), "INR", bill.getCustomerName(), booking.getGuest().getEmail());

        log.info("Payment Session Created Successfully with sessionId: {}", session.getId());
        bill.setSessionId(session.getId());

        // Save bill
        billRepository.save(bill);
        log.info("Bill Saved Successfully with ID: {}", bill.getId());

        // Prepare and return payment response
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setCustomerName(booking.getGuest().getFullName());
        paymentResponse.setSessionId(session.getId());
        paymentResponse.setSessionUrl(session.getUrl());

        log.info("Returning payment response for booking ID: {}", bookingId);
        return paymentResponse;
    }

    @Override
    public BillResponseDTO getBill(Long id) {
        log.info("Fetching bill for ID: {}", id);
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        log.info("Bill found for ID: {}", id);
        return modelMapper.map(bill, BillResponseDTO.class);
    }

    @Override
    public List<BillResponseDTO> getAllBills() {
        log.info("Fetching all bills");
        List<BillResponseDTO> bills = billRepository.findAll()
                .stream()
                .map(bill -> modelMapper.map(bill, BillResponseDTO.class))
                .collect(Collectors.toList());
        log.info("Total bills found: {}", bills.size());
        return bills;
    }
}
