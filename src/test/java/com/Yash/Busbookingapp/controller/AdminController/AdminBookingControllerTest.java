package com.Yash.Busbookingapp.controller.AdminController;

import com.Yash.Busbookingapp.controller.admincontroller.AdminBookingController;
import com.Yash.Busbookingapp.model.Booking;
import com.Yash.Busbookingapp.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminBookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private AdminBookingController adminBookingController;

    private Booking booking1;
    private Booking booking2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        booking1 = new Booking();
        booking1.setId(1L);
        booking1.setPassengerName("Alice");
        booking1.setContactInfo("alice@example.com");
        booking1.setSeatCount(2);
        booking1.setSeatPreference("Window");

        booking2 = new Booking();
        booking2.setId(2L);
        booking2.setPassengerName("Bob");
        booking2.setContactInfo("bob@example.com");
        booking2.setSeatCount(1);
        booking2.setSeatPreference("Aisle");
    }

    @Test
    void testGetAllBookings() {
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(booking1, booking2));

        ResponseEntity<List<Booking>> response = adminBookingController.getAllBookings();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    void testGetBookingById_Found() {
        when(bookingService.getBookingById(1L)).thenReturn(booking1);

        ResponseEntity<Booking> response = adminBookingController.getBookingById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Alice", response.getBody().getPassengerName());
        verify(bookingService, times(1)).getBookingById(1L);
    }

    @Test
    void testGetBookingById_NotFound() {
        when(bookingService.getBookingById(99L)).thenReturn(null);

        ResponseEntity<Booking> response = adminBookingController.getBookingById(99L);

        assertEquals(404, response.getStatusCodeValue());
        verify(bookingService, times(1)).getBookingById(99L);
    }

    @Test
    void testDeleteBooking_Found() {
        when(bookingService.getBookingById(1L)).thenReturn(booking1);
        doNothing().when(bookingService).deleteBooking(1L);

        ResponseEntity<Void> response = adminBookingController.deleteBooking(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(bookingService, times(1)).getBookingById(1L);
        verify(bookingService, times(1)).deleteBooking(1L);
    }

    @Test
    void testDeleteBooking_NotFound() {
        when(bookingService.getBookingById(99L)).thenReturn(null);

        ResponseEntity<Void> response = adminBookingController.deleteBooking(99L);

        assertEquals(404, response.getStatusCodeValue());
        verify(bookingService, times(1)).getBookingById(99L);
        verify(bookingService, never()).deleteBooking(anyLong());
    }

    @Test
    void testUpdateBooking_Found() {
        Booking updated = new Booking();
        updated.setPassengerName("Alice Updated");
        updated.setContactInfo("alice2@example.com");
        updated.setSeatCount(3);
        updated.setSeatPreference("No Preference");

        when(bookingService.getBookingById(1L)).thenReturn(booking1);
        when(bookingService.updateBooking(1L, booking1)).thenReturn(updated);

        ResponseEntity<Booking> response = adminBookingController.updateBooking(1L, updated);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Alice Updated", response.getBody().getPassengerName());
        assertEquals("No Preference", response.getBody().getSeatPreference());
        verify(bookingService, times(1)).getBookingById(1L);
        verify(bookingService, times(1)).updateBooking(1L, booking1);
    }

    @Test
    void testUpdateBooking_NotFound() {
        Booking updated = new Booking();
        when(bookingService.getBookingById(99L)).thenReturn(null);

        ResponseEntity<Booking> response = adminBookingController.updateBooking(99L, updated);

        assertEquals(404, response.getStatusCodeValue());
        verify(bookingService, times(1)).getBookingById(99L);
        verify(bookingService, never()).updateBooking(anyLong(), any());
    }
}
