package com.example.assignment_pedro_silva.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "BOOKS")
public class Book {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID uuid;
    @Column
    private String title;
    @Column
    private String author;
    @Column
    private String isbn;
    @Column
    private Integer numberOfAvailableCopies;
    @Column
    private Integer numberOfActiveReservations;

    public Book(String title, String author, String isbn, Integer numberOfAvailableCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.numberOfAvailableCopies = numberOfAvailableCopies;
        this.numberOfActiveReservations = 0;
    }

    public Book() {}

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getNumberOfAvailableCopies() {
        return numberOfAvailableCopies;
    }

    public void setNumberOfAvailableCopies(Integer numberOfAvailableCopies) {
        this.numberOfAvailableCopies = numberOfAvailableCopies;
    }

    public Integer getNumberOfActiveReservations() {
        return numberOfActiveReservations;
    }

    public void setNumberOfActiveReservations(Integer numberOfActiveReservations) {
        this.numberOfActiveReservations = numberOfActiveReservations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return Objects.equals(uuid, book.uuid) && Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(isbn, book.isbn) && Objects.equals(numberOfAvailableCopies, book.numberOfAvailableCopies) && Objects.equals(numberOfActiveReservations, book.numberOfActiveReservations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, title, author, isbn, numberOfAvailableCopies, numberOfActiveReservations);
    }

    @Override
    public String toString() {
        return "Book{" +
                "uuid=" + uuid +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", numberOfCopies=" + numberOfAvailableCopies +
                ", numberOfActiveReservations=" + numberOfActiveReservations +
                '}';
    }
}
