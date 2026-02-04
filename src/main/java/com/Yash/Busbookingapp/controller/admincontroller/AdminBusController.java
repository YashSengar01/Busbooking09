package com.Yash.Busbookingapp.controller.admincontroller;

import com.Yash.Busbookingapp.model.Bus;
import com.Yash.Busbookingapp.service.BusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/buses")
public class AdminBusController {

    @Autowired
    private BusService busService;

    // ✅ Get all buses
    @GetMapping
    public ResponseEntity<List<Bus>> getAllBuses() {
        return ResponseEntity.ok(busService.getAllBuses());
    }

    // ✅ Get a bus by ID
    @GetMapping("/{id}")
    public ResponseEntity<Bus> getBusById(@PathVariable Long id) {
        Bus bus = busService.getBusById(id);
        if (bus == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bus);
    }

    // ✅ Add a new bus
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Bus> addBus(@RequestBody Bus newBus) {
        Bus createdBus = busService.saveBus(newBus);
        return ResponseEntity.ok(createdBus);
    }

    // ✅ Update a bus
    @PutMapping("/{id}")
    public ResponseEntity<Bus> updateBus(@PathVariable Long id, @RequestBody Bus updatedBus) {
        Bus existingBus = busService.getBusById(id);
        if (existingBus == null) {
            return ResponseEntity.notFound().build();
        }

        // Update fields
        existingBus.setFromLocation(updatedBus.getFromLocation());
        existingBus.setToLocation(updatedBus.getToLocation());
        existingBus.setDepartureTime(updatedBus.getDepartureTime());
        existingBus.setArrivalTime(updatedBus.getArrivalTime());
        existingBus.setPrice(updatedBus.getPrice());
        existingBus.setPrice(updatedBus.getTotalSeats());

        Bus savedBus = busService.updateBus(id, existingBus);
        return ResponseEntity.ok(savedBus);
    }

    // ✅ Delete a bus
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        Bus bus = busService.getBusById(id);
        if (bus == null) {
            return ResponseEntity.notFound().build();
        }
        busService.deleteBus(id);
        return ResponseEntity.noContent().build();
    }
}
