package com.Yash.Busbookingapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromLocation;
    private String toLocation;
    private String departureTime;
    private String arrivalTime;
    private double price;
    private int totalSeats;

    @OneToMany(mappedBy = "bus")
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<Booking> bookings;

}
