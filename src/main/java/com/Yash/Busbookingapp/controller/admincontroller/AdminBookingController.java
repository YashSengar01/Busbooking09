package com.Yash.Busbookingapp.controller.admincontroller;

import com.Yash.Busbookingapp.model.Booking;
import com.Yash.Busbookingapp.service.BookingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/bookings")
public class AdminBookingController {

    @Autowired
    private BookingService bookingService;

    // ✅ Get all bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // ✅ Get a single booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(booking);
    }

    // ✅ Delete a booking by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ (Optional) Update a booking
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        Booking existing = bookingService.getBookingById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        // Update relevant fields
        existing.setPassengerName(updatedBooking.getPassengerName());
        existing.setContactInfo(updatedBooking.getContactInfo());
        existing.setSeatCount(updatedBooking.getSeatCount());
        existing.setSeatPreference(updatedBooking.getSeatPreference());

        Booking saved = bookingService.updateBooking(id, existing);
        return ResponseEntity.ok(saved);
    }
}
