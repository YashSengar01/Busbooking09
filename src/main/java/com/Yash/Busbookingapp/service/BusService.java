package com.Yash.Busbookingapp.service;

import com.Yash.Busbookingapp.model.Bus;

import java.util.List;

public interface BusService {

    List<Bus> getAllBuses();

    Bus saveBus(Bus bus);

    Bus getBusById(Long id);

    void deleteBus(Long id);

    Bus updateBus(Long id, Bus updatedBus);
}
