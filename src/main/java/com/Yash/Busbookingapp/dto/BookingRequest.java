package com.Yash.Busbookingapp.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private String passengerName;
    private String contactInfo;
    private int seatCount;
    private String seatPreference;
    private Long busId;
}
