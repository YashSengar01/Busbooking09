package com.Yash.Busbookingapp.controller;

import com.Yash.Busbookingapp.dto.BookingConfirmationDto;
import com.Yash.Busbookingapp.dto.BookingRequest;
import com.Yash.Busbookingapp.model.Booking;
import com.Yash.Busbookingapp.model.Bus;
import com.Yash.Busbookingapp.service.BookingService;
import com.Yash.Busbookingapp.service.TicketService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private Authentication authentication;

    private Booking booking;
    private BookingRequest bookingRequest;
    private Bus bus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bus = new Bus();
        bus.setId(1L);
        bus.setFromLocation("CityA");
        bus.setToLocation("CityB");

        booking = new Booking();
        booking.setId(10L);
        booking.setPassengerName("Alice");
        booking.setContactInfo("alice@example.com");
        booking.setSeatCount(2);
        booking.setSeatPreference("Window");
        booking.setBus(bus);

        bookingRequest = new BookingRequest();
        bookingRequest.setBusId(1L);
        bookingRequest.setPassengerName("Alice");
        bookingRequest.setContactInfo("alice@example.com");
        bookingRequest.setSeatCount(2);
        bookingRequest.setSeatPreference("Window");
    }

    // -------------------- CREATE BOOKING --------------------
    @Test
    void testCreateBooking() {
        when(authentication.getName()).thenReturn("alice@example.com");
        when(bookingService.createBooking(any(Booking.class), eq(1L), eq("alice@example.com")))
                .thenReturn(booking);

        ResponseEntity<BookingConfirmationDto> response = bookingController.createBooking(bookingRequest,
                authentication);

        assertEquals(201, response.getStatusCodeValue());
        BookingConfirmationDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Alice", body.getPassengerName());
        assertEquals("CityA", body.getFromLocation());
        assertEquals("CityB", body.getToLocation());
        assertEquals("✅ Booking Confirmed", body.getMessage());

        verify(bookingService, times(1))
                .createBooking(any(Booking.class), eq(1L), eq("alice@example.com"));
    }

    // -------------------- DOWNLOAD TICKET --------------------
    @Test
    void testDownloadTicket() {
        byte[] pdfBytes = new byte[] { 1, 2, 3 };
        when(authentication.getName()).thenReturn("alice@example.com");
        when(ticketService.generatePdf(10L, "alice@example.com")).thenReturn(pdfBytes);

        ResponseEntity<byte[]> response = bookingController.downloadTicket(10L, authentication);

        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(pdfBytes, response.getBody());
        assertTrue(response.getHeaders().getContentDisposition().toString().contains("ticket.pdf"));
        verify(ticketService, times(1)).generatePdf(10L, "alice@example.com");
    }

    // -------------------- GET MY BOOKINGS --------------------
    @Test
    void testGetMyBookings() {
        when(authentication.getName()).thenReturn("alice@example.com");
        when(bookingService.getBookingsForUser("alice@example.com")).thenReturn(Arrays.asList(booking));

        ResponseEntity<List<Booking>> response = bookingController.getMyBookings(authentication);

        assertEquals(200, response.getStatusCodeValue());
        List<Booking> bookings = response.getBody();
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals("Alice", bookings.get(0).getPassengerName());

        verify(bookingService, times(1)).getBookingsForUser("alice@example.com");
    }

    // -------------------- GET BOOKING BY ID --------------------
    @Test
    void testGetBookingById_Found() {
        when(bookingService.getBookingById(10L)).thenReturn(booking);

        ResponseEntity<Booking> response = bookingController.getBookingById(10L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Alice", response.getBody().getPassengerName());
        verify(bookingService, times(1)).getBookingById(10L);
    }

    @Test
    void testGetBookingById_NotFound() {
        when(bookingService.getBookingById(99L)).thenReturn(null);

        ResponseEntity<Booking> response = bookingController.getBookingById(99L);

        assertEquals(404, response.getStatusCodeValue());
        verify(bookingService, times(1)).getBookingById(99L);
    }
}
