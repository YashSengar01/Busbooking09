package com.Yash.Busbookingapp.service;

import com.Yash.Busbookingapp.model.Bus;
import com.Yash.Busbookingapp.repository.BusRepository;
import com.Yash.Busbookingapp.service.impl.BusServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusServiceImplTest {

    @Mock
    private BusRepository busRepository;

    @InjectMocks
    private BusServiceImpl busService;

    private Bus bus1;
    private Bus bus2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bus1 = new Bus();
        bus1.setId(1L);
        bus1.setFromLocation("City A");
        bus1.setToLocation("City B");
        bus1.setDepartureTime("10:00");
        bus1.setArrivalTime("14:00");
        bus1.setPrice(500);
        bus1.setTotalSeats(40);

        bus2 = new Bus();
        bus2.setId(2L);
        bus2.setFromLocation("City X");
        bus2.setToLocation("City Y");
        bus2.setDepartureTime("12:00");
        bus2.setArrivalTime("16:00");
        bus2.setPrice(700);
        bus2.setTotalSeats(50);
    }

    // -------------------- GET ALL BUSES --------------------
    @Test
    void testGetAllBuses() {
        when(busRepository.findAll()).thenReturn(Arrays.asList(bus1, bus2));

        List<Bus> buses = busService.getAllBuses();

        assertEquals(2, buses.size());
        verify(busRepository, times(1)).findAll();
    }

    // -------------------- SAVE BUS --------------------
    @Test
    void testSaveBus() {
        when(busRepository.save(bus1)).thenReturn(bus1);

        Bus saved = busService.saveBus(bus1);

        assertNotNull(saved);
        assertEquals("City A", saved.getFromLocation());
        verify(busRepository, times(1)).save(bus1);
    }

    // -------------------- GET BUS BY ID --------------------
    @Test
    void testGetBusById_Found() {
        when(busRepository.findById(1L)).thenReturn(Optional.of(bus1));

        Bus found = busService.getBusById(1L);

        assertNotNull(found);
        assertEquals("City A", found.getFromLocation());
        verify(busRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBusById_NotFound() {
        when(busRepository.findById(99L)).thenReturn(Optional.empty());

        Bus found = busService.getBusById(99L);

        assertNull(found);
        verify(busRepository, times(1)).findById(99L);
    }

    // -------------------- DELETE BUS --------------------
    @Test
    void testDeleteBus() {
        doNothing().when(busRepository).deleteById(1L);

        busService.deleteBus(1L);

        verify(busRepository, times(1)).deleteById(1L);
    }

    // -------------------- UPDATE BUS --------------------
    @Test
    void testUpdateBus_Found() {
        Bus updatedBus = new Bus();
        updatedBus.setFromLocation("City Updated");
        updatedBus.setToLocation("City Z");
        updatedBus.setDepartureTime("15:00");
        updatedBus.setArrivalTime("18:00");
        updatedBus.setPrice(800);
        updatedBus.setTotalSeats(60);

        when(busRepository.findById(1L)).thenReturn(Optional.of(bus1));
        when(busRepository.save(any(Bus.class))).thenReturn(updatedBus);

        Bus result = busService.updateBus(1L, updatedBus);

        assertNotNull(result);
        assertEquals("City Updated", result.getFromLocation());
        assertEquals(60, result.getTotalSeats());
        verify(busRepository, times(1)).findById(1L);
        verify(busRepository, times(1)).save(bus1);
    }

    @Test
    void testUpdateBus_NotFound() {
        Bus updatedBus = new Bus();
        when(busRepository.findById(99L)).thenReturn(Optional.empty());

        Bus result = busService.updateBus(99L, updatedBus);

        assertNull(result);
        verify(busRepository, times(1)).findById(99L);
        verify(busRepository, never()).save(any(Bus.class));
    }
}
