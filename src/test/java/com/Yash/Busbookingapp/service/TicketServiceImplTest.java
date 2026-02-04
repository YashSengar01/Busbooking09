package com.Yash.Busbookingapp.service;

import com.Yash.Busbookingapp.model.Booking;
import com.Yash.Busbookingapp.model.Bus;
import com.Yash.Busbookingapp.model.Users;
import com.Yash.Busbookingapp.repository.BookingRepository;
import com.Yash.Busbookingapp.service.impl.TicketServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private Users user;
    private Bus bus;
    private Booking booking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new Users();
        user.setId(1L);
        user.setUsername("alice");

        bus = new Bus();
        bus.setId(100L);

        booking = new Booking();
        booking.setId(10L);
        booking.setPassengerName("Alice");
        booking.setContactInfo("alice@example.com");
        booking.setSeatCount(2);
        booking.setSeatPreference("Window");
        booking.setBus(bus);
        booking.setUser(user);
    }

    // -------------------- SUCCESS --------------------
    @Test
    void testGeneratePdf_Success() {
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));

        byte[] pdfBytes = ticketService.generatePdf(10L, "alice");

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);

        verify(bookingRepository, times(1)).findById(10L);
    }

    // -------------------- BOOKING NOT FOUND --------------------
    @Test
    void testGeneratePdf_BookingNotFound() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> ticketService.generatePdf(99L, "alice"));

        assertEquals("Booking not found", exception.getMessage());
        verify(bookingRepository, times(1)).findById(99L);
    }

    // -------------------- UNAUTHORIZED ACCESS --------------------
    @Test
    void testGeneratePdf_Unauthorized() {
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> ticketService.generatePdf(10L, "bob") // wrong username
        );

        assertEquals("Unauthorized access to ticket", exception.getMessage());
        verify(bookingRepository, times(1)).findById(10L);
    }
}
