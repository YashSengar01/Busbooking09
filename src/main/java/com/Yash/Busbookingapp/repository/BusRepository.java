package com.Yash.Busbookingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Yash.Busbookingapp.model.Bus;

import org.springframework.stereotype.Repository;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
}
