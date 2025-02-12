package com.example.assignment_pedro_silva.service;

import com.example.assignment_pedro_silva.dto.BookDTO;
import com.example.assignment_pedro_silva.dto.ReservationDTO;
import com.example.assignment_pedro_silva.exceptions.BookNotAvailableException;
import com.example.assignment_pedro_silva.exceptions.ExceededNumberOfReservationsException;
import com.example.assignment_pedro_silva.exceptions.ResourceNotFoundException;
import com.example.assignment_pedro_silva.model.Book;
import com.example.assignment_pedro_silva.model.Reservation;
import com.example.assignment_pedro_silva.model.ReservationStatus;
import com.example.assignment_pedro_silva.model.User;
import com.example.assignment_pedro_silva.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookService bookService;
    private final UserService userService;

    public ReservationService(ReservationRepository reservationRepository, BookService bookService, UserService userService) {
        this.reservationRepository = reservationRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    public ReservationDTO saveReservation(ReservationDTO reservationDTO, User user) throws BookNotAvailableException, ExceededNumberOfReservationsException {
        if (userService.hasReachedMaximumNumberOfReservations(user)) {
            throw new ExceededNumberOfReservationsException(String.format("User %s exceed number of reservations. Cancel any reservation and try again.", user.getUuid()));
        }

        Book book = bookService.findBook(UUID.fromString(reservationDTO.getBookDTO().getUuid()));
        if (book.getNumberOfAvailableCopies() < book.getNumberOfActiveReservations()) {
            throw new BookNotAvailableException(String.format("Book %s does not have available copies.", book.getTitle()));
        }

        Book updatedBook = bookService.reserveCopy(book);
        Reservation reservation = new Reservation(updatedBook, LocalDateTime.now());
        reservation.setUser(user);
        Reservation persistedReservation = reservationRepository.save(reservation);
        userService.addReservation(user, reservation);

        return createDTO(persistedReservation);
    }

    public ReservationDTO findReservation(UUID uuid) {
        try {
            Reservation reservation = reservationRepository.getReferenceById(uuid);
            ReservationDTO dto = createDTO(reservation);
            return dto;
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(String.format("Reservation with id %s not found.", uuid.toString()));
        }
    }

    public void cancelReservation(UUID uuid) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(uuid);
        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            Book book = bookService.findBook(reservation.getBook().getUuid());
            bookService.freeCopy(book);

            reservation.setReservationStatus(ReservationStatus.CANCELED);
            reservationRepository.save(reservation);
        } else {
            throw new ResourceNotFoundException("Reservation not found.");
        }
    }

    public List<ReservationDTO> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<ReservationDTO> reservationDTOs = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationDTOs.add(createDTO(reservation));
        }
        return reservationDTOs;
    }

    public List<ReservationDTO> findByUserUuid(UUID uuid) {
        List<Reservation> reservations = reservationRepository.findByUserUuid(uuid);
        List<ReservationDTO> reservationDTOs = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationDTOs.add(createDTO(reservation));
        }
        return reservationDTOs;
    }

    private ReservationDTO createDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationStatus(reservation.getReservationStatus());
        dto.setBookDTO(new BookDTO(reservation.getBook().getUuid().toString(), reservation.getBook().getTitle(), reservation.getBook().getAuthor()));
        dto.setUuid(reservation.getUuid().toString());
        dto.setUserUuid(reservation.getUser().getUuid().toString());
        return dto;
    }
}
