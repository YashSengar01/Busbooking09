package com.Yash.Busbookingapp.repository;

import com.Yash.Busbookingapp.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserUsername(String username);
}
