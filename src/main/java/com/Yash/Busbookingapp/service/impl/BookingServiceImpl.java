package com.Yash.Busbookingapp.service.impl;

import com.Yash.Busbookingapp.model.Booking;
import com.Yash.Busbookingapp.model.Bus;
import com.Yash.Busbookingapp.model.Users;
import com.Yash.Busbookingapp.repository.BookingRepository;
import com.Yash.Busbookingapp.repository.BusRepository;
import com.Yash.Busbookingapp.repository.UserRepository;
import com.Yash.Busbookingapp.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Override
    public Booking createBooking(Booking booking, Long busId, String userEmail) {

        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new RuntimeException("Bus not found with id: " + busId));

        Users user = userRepository.findByUsername(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        booking.setBus(bus);
        booking.setUser(user); // ✅ VERY IMPORTANT

        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public Booking updateBooking(Long id, Booking existing) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setPassengerName(existing.getPassengerName());
        booking.setContactInfo(existing.getContactInfo());
        booking.setSeatCount(existing.getSeatCount());
        booking.setSeatPreference(existing.getSeatPreference());

        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsForUser(String username) {
        return bookingRepository.findByUserUsername(username);
    }

}
