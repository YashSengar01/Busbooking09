package com.Yash.Busbookingapp.service.impl;

import com.Yash.Busbookingapp.model.Bus;
import com.Yash.Busbookingapp.repository.BusRepository;
import com.Yash.Busbookingapp.service.BusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusServiceImpl implements BusService {

    @Autowired
    private BusRepository busRepository;

    @Override
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    @Override
    public Bus saveBus(Bus bus) {
        return busRepository.save(bus);
    }

    @Override
    public Bus getBusById(Long id) {
        return busRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }

    @Override
    public Bus updateBus(Long id, Bus updatedBus) {
        Optional<Bus> optionalBus = busRepository.findById(id);
        if (optionalBus.isPresent()) {
            Bus existingBus = optionalBus.get();
            existingBus.setFromLocation(updatedBus.getFromLocation());
            existingBus.setToLocation(updatedBus.getToLocation());
            existingBus.setDepartureTime(updatedBus.getDepartureTime());
            existingBus.setArrivalTime(updatedBus.getArrivalTime());
            existingBus.setPrice(updatedBus.getPrice());
            existingBus.setTotalSeats(updatedBus.getTotalSeats());
            return busRepository.save(existingBus);
        } else {
            return null;
        }
    }

}
