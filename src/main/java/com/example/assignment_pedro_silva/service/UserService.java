package com.example.assignment_pedro_silva.service;

import com.example.assignment_pedro_silva.model.Reservation;
import com.example.assignment_pedro_silva.model.ReservationStatus;
import com.example.assignment_pedro_silva.model.User;
import com.example.assignment_pedro_silva.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User find(UUID uuid) {
        return userRepository.getReferenceById(uuid);
    }

    public User addReservation(User user, Reservation reservation) {
        user.getReservations().add(reservation);
        return save(user);
    }

    public boolean hasReachedMaximumNumberOfReservations(User user) {
        User userLoaded = find(user.getUuid());
        int activeReservations = 0;
        for (Reservation reservation : userLoaded.getReservations()) {
            if (ReservationStatus.ACTIVE.equals(reservation.getReservationStatus())) {
                activeReservations++;
            }
        }
        return activeReservations >= 3 ? true : false;
    }
}
