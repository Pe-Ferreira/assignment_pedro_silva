package com.example.assignment_pedro_silva.repository;

import com.example.assignment_pedro_silva.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByUserUuid(UUID uuid);
}
