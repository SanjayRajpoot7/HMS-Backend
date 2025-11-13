package com.example.billservice.service.imple;

import com.example.billservice.dto.BillResponseDTO;
import com.example.billservice.dto.BookingResponseDTO;
import com.example.billservice.dto.PaymentResponse;
import com.example.billservice.entity.Bill;
import com.example.billservice.exception.BillAlreadyPaidException;
import com.example.billservice.exception.ResourceNotFoundException;
import com.example.billservice.repository.BillRepository;
import com.example.billservice.service.BookingServiceClient;
//import com.example.billservice.service.PaymentService;
import com.stripe.model.checkout.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillServiceImplTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PaymentService paymentService;

    @Mock
    private BookingServiceClient bookingServiceClient;

    @InjectMocks
    private BillServiceImpl billService;

    private Bill bill;
    private BookingResponseDTO bookingResponseDTO;
    private PaymentResponse paymentResponse;
    private BillResponseDTO billResponseDTO;
    private Session session;

    @BeforeEach
    void setUp() {
        bookingResponseDTO = new BookingResponseDTO();
        bookingResponseDTO.setBookingId(1L);
        bookingResponseDTO.setCheckinDate(LocalDate.now());
        bookingResponseDTO.setCheckoutDate(LocalDate.now().plusDays(3));

        bill = new Bill();
        bill.setId(1L);
        bill.setBookingId(1L);
        bill.setCustomerName("John Doe");
        bill.setTotalAmount(6000.0);
        bill.setPaymentMode("CARD");
        bill.setPaymentStatus("PENDING");
        bill.setPaymentDate(LocalDateTime.now());

        billResponseDTO = new BillResponseDTO();
        billResponseDTO.setBillId(1L);
        billResponseDTO.setBookingId(1L);
        billResponseDTO.setCustomerName("John Doe");
        billResponseDTO.setTotalAmount(6000.0);
        billResponseDTO.setPaymentStatus("PENDING");

        session = new Session();
        session.setId("session123");
        session.setUrl("https://checkout.stripe.com/session123");

        paymentResponse = new PaymentResponse();
        paymentResponse.setCustomerName("John Doe");
        paymentResponse.setSessionId("session123");
        paymentResponse.setSessionUrl("https://checkout.stripe.com/session123");
    }

    @Test
    void createBill_ShouldReturnPaymentResponse() throws Exception {
        when(bookingServiceClient.getBooking(1L)).thenReturn(bookingResponseDTO);
        when(billRepository.findByBookingId(1L)).thenReturn(Optional.empty());
        when(paymentService.createCheckoutSession(anyDouble(), anyString(), anyString(), anyString())).thenReturn(session);
        when(billRepository.save(any(Bill.class))).thenReturn(bill);

        PaymentResponse response = billService.createBill(1L);

        assertNotNull(response);
        assertEquals("John Doe", response.getCustomerName());
        assertEquals("session123", response.getSessionId());
    }

    @Test
    void createBill_ShouldThrowExceptionWhenBillAlreadyPaid() {
        bill.setPaymentStatus("SUCCESS");
        when(billRepository.findByBookingId(1L)).thenReturn(Optional.of(bill));

        assertThrows(BillAlreadyPaidException.class, () -> billService.createBill(1L));
    }

    @Test
    void getBill_ShouldReturnBillResponseDTO() {
        when(billRepository.findById(1L)).thenReturn(Optional.of(bill));
        when(modelMapper.map(bill, BillResponseDTO.class)).thenReturn(billResponseDTO);

        BillResponseDTO response = billService.getBill(1L);

        assertNotNull(response);
        assertEquals(1L, response.getBillId());
        assertEquals("John Doe", response.getCustomerName());
    }

    @Test
    void getBill_ShouldThrowExceptionWhenBillNotFound() {
        when(billRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> billService.getBill(1L));
    }
}