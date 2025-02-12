package com.example.assignment_pedro_silva.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "RESERVATIONS")
public class Reservation {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID uuid;
    @ManyToOne
    @JoinColumn(name = "USER_UUID", nullable = false)
    @JsonManagedReference
    private User user;
    @ManyToOne
    @JoinColumn
    private Book book;
    @Column
    private LocalDateTime createdAt;
    @Column
    private ReservationStatus reservationStatus;

    public Reservation(Book book, LocalDateTime createdAt) {
        this.book = book;
        this.createdAt = createdAt;
        this.reservationStatus = ReservationStatus.ACTIVE;
    }

    public Reservation(){}

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation that)) return false;
        return Objects.equals(uuid, that.uuid) && Objects.equals(user, that.user) && Objects.equals(book, that.book) && Objects.equals(createdAt, that.createdAt) && reservationStatus == that.reservationStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, user, book, createdAt, reservationStatus);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "uuid=" + uuid +
                ", user=" + user +
                ", book=" + book +
                ", createdAt=" + createdAt +
                ", reservationStatus=" + reservationStatus +
                '}';
    }
}
