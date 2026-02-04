package com.Yash.Busbookingapp.service;

public interface TicketService {
    byte[] generatePdf(Long bookingId, String username);
}
