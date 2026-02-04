package com.Yash.Busbookingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingConfirmationDto {

    private Long bookingId;
    private String passengerName;
    private String fromLocation;
    private String toLocation;
    private int seatCount;
    private String seatPreference;
    private String message;
}
