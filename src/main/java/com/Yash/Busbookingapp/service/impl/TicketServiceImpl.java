package com.Yash.Busbookingapp.service.impl;

import com.Yash.Busbookingapp.model.Booking;
import com.Yash.Busbookingapp.repository.BookingRepository;
import com.Yash.Busbookingapp.service.TicketService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public byte[] generatePdf(Long bookingId, String username) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // 🔒 Security check (VERY IMPORTANT)
        if (!booking.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to ticket");
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("🚌 Bus Ticket", titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Passenger: " + booking.getPassengerName(), bodyFont));
            document.add(new Paragraph("Contact: " + booking.getContactInfo(), bodyFont));
            document.add(new Paragraph("Seats: " + booking.getSeatCount(), bodyFont));
            document.add(new Paragraph("Preference: " + booking.getSeatPreference(), bodyFont));
            document.add(new Paragraph("Bus ID: " + booking.getBus().getId(), bodyFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Thank you for booking with BusBooking!", bodyFont));

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate ticket PDF", e);
        }
    }
}
