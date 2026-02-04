package com.Yash.Busbookingapp.service;

import com.Yash.Busbookingapp.model.Booking;

import java.util.List;

public interface BookingService {

    List<Booking> getAllBookings();

    Booking getBookingById(Long id);

    List<Booking> findAll();

    Booking save(Booking booking);

    Booking createBooking(Booking booking, Long busId, String userEmail);

    void deleteBooking(Long id);

    Booking updateBooking(Long id, Booking existing);

    List<Booking> getBookingsForUser(String email);
}
