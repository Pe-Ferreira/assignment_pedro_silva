package com.example.assignment_pedro_silva.repository;

import com.example.assignment_pedro_silva.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
