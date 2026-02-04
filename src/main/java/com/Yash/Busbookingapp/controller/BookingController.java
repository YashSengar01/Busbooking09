package com.Yash.Busbookingapp.controller;

import com.Yash.Busbookingapp.dto.BookingConfirmationDto;
import com.Yash.Busbookingapp.dto.BookingRequest;
import com.Yash.Busbookingapp.model.Booking;
import com.Yash.Busbookingapp.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.Yash.Busbookingapp.service.TicketService;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TicketService ticketService;

    // ✅ CREATE BOOKING (USER BASED)
    @PostMapping
    public ResponseEntity<BookingConfirmationDto> createBooking(
            @RequestBody BookingRequest bookingRequest,
            Authentication authentication) {

        String email = authentication.getName();

        Booking booking = new Booking();
        booking.setPassengerName(bookingRequest.getPassengerName());
        booking.setContactInfo(bookingRequest.getContactInfo());
        booking.setSeatCount(bookingRequest.getSeatCount());
        booking.setSeatPreference(bookingRequest.getSeatPreference());

        Booking saved = bookingService.createBooking(
                booking,
                bookingRequest.getBusId(),
                email);

        BookingConfirmationDto response = new BookingConfirmationDto(
                saved.getId(),
                saved.getPassengerName(),
                saved.getBus().getFromLocation(),
                saved.getBus().getToLocation(),
                saved.getSeatCount(),
                saved.getSeatPreference(),
                "✅ Booking Confirmed");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/ticket")
    public ResponseEntity<byte[]> downloadTicket(
            @PathVariable Long id,
            Authentication auth) {

        byte[] pdf = ticketService.generatePdf(id, auth.getName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=ticket.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getMyBookings(Authentication authentication) {

        String username = authentication.getName(); // ✅ username from JWT
        return ResponseEntity.ok(bookingService.getBookingsForUser(username));
    }

    // ❌ OPTIONAL: REMOVE OR ADMIN-ONLY
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        return booking != null
                ? ResponseEntity.ok(booking)
                : ResponseEntity.notFound().build();
    }
}