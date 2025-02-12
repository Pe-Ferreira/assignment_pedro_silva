package com.example.assignment_pedro_silva.repository;

import com.example.assignment_pedro_silva.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
}
