package com.Yash.Busbookingapp.config;

import com.Yash.Busbookingapp.model.Bus;
import com.Yash.Busbookingapp.repository.BusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initBusData(BusRepository busRepository) {
        return args -> {

            // Prevent duplicate insert on every restart
            if (busRepository.count() > 0) {
                return;
            }

            Bus bus1 = new Bus(
                    null,
                    "Delhi",
                    "Agra",
                    "06:00 AM",
                    "09:30 AM",
                    450.00,
                    40,
                    null
            );

            Bus bus2 = new Bus(
                    null,
                    "Noida",
                    "Jaipur",
                    "08:00 AM",
                    "02:00 PM",
                    750.00,
                    45,
                    null
            );

            Bus bus3 = new Bus(
                    null,
                    "Ghaziabad",
                    "Lucknow",
                    "10:00 PM",
                    "06:00 AM",
                    1200.00,
                    50,
                    null
            );

            busRepository.saveAll(List.of(bus1, bus2, bus3));

            System.out.println("🚌 Sample Bus data inserted into MySQL successfully!");
        };
    }
}
