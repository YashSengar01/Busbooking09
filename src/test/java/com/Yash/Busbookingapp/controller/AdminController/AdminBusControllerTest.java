package com.Yash.Busbookingapp.controller.AdminController;

import com.Yash.Busbookingapp.controller.admincontroller.AdminBusController;
import com.Yash.Busbookingapp.model.Bus;
import com.Yash.Busbookingapp.service.BusService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminBusController.class)
class AdminBusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusService busService;

    @Autowired
    private ObjectMapper objectMapper;

    private Bus bus1;
    private Bus bus2;

    @BeforeEach
    void setUp() {
        bus1 = new Bus();
        bus1.setId(1L);
        bus1.setFromLocation("CityA");
        bus1.setToLocation("CityB");
        bus1.setDepartureTime("2026-02-03T10:00");
        bus1.setArrivalTime("2026-02-03T14:00");
        bus1.setPrice(500.0);
        bus1.setTotalSeats(40);

        bus2 = new Bus();
        bus2.setId(2L);
        bus2.setFromLocation("CityC");
        bus2.setToLocation("CityD");
        bus2.setDepartureTime("2026-02-03T10:00");
        bus2.setArrivalTime("2026-02-03T14:00");
        bus2.setPrice(600.0);
        bus2.setTotalSeats(50);
    }

    @Test
    void testGetAllBuses() throws Exception {
        List<Bus> buses = Arrays.asList(bus1, bus2);
        Mockito.when(busService.getAllBuses()).thenReturn(buses);

        mockMvc.perform(get("/admin/api/buses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].fromLocation").value("CityA"))
                .andExpect(jsonPath("$[1].toLocation").value("CityD"));
    }

    @Test
    void testGetBusById_Found() throws Exception {
        Mockito.when(busService.getBusById(1L)).thenReturn(bus1);

        mockMvc.perform(get("/admin/api/buses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromLocation").value("CityA"))
                .andExpect(jsonPath("$.toLocation").value("CityB"));
    }

    @Test
    void testGetBusById_NotFound() throws Exception {
        Mockito.when(busService.getBusById(99L)).thenReturn(null);

        mockMvc.perform(get("/admin/api/buses/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddBus() throws Exception {
        Bus newBus = bus1;
        Mockito.when(busService.saveBus(any(Bus.class))).thenReturn(newBus);

        mockMvc.perform(post("/admin/api/buses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBus)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromLocation").value("CityA"))
                .andExpect(jsonPath("$.price").value(500.0));
    }

    @Test
    void testUpdateBus_Found() throws Exception {
        Bus updatedBus = bus1;
        updatedBus.setPrice(700.0);

        Mockito.when(busService.getBusById(1L)).thenReturn(bus1);
        Mockito.when(busService.updateBus(eq(1L), any(Bus.class))).thenReturn(updatedBus);

        mockMvc.perform(put("/admin/api/buses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBus)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(700.0));
    }

    @Test
    void testUpdateBus_NotFound() throws Exception {
        Mockito.when(busService.getBusById(99L)).thenReturn(null);

        mockMvc.perform(put("/admin/api/buses/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bus1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBus_Found() throws Exception {
        Mockito.when(busService.getBusById(1L)).thenReturn(bus1);

        mockMvc.perform(delete("/admin/api/buses/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteBus_NotFound() throws Exception {
        Mockito.when(busService.getBusById(99L)).thenReturn(null);

        mockMvc.perform(delete("/admin/api/buses/99"))
                .andExpect(status().isNotFound());
    }
}
