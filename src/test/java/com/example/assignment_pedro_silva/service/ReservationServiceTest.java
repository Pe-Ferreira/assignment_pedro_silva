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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private BookService bookService;
    @Mock
    private UserService userService;
    @InjectMocks
    private ReservationService reservationService;
    private User user;
    private Book book;
    private Reservation reservation;
    private UUID bookUuid;
    private UUID userUuid;
    private UUID reservationUuid;

    @BeforeEach
    void setUp() {
        bookUuid = UUID.randomUUID();
        userUuid = UUID.randomUUID();
        reservationUuid = UUID.randomUUID();

        book = new Book();
        book.setUuid(bookUuid);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setNumberOfAvailableCopies(2);
        book.setNumberOfActiveReservations(1);

        user = new User();
        user.setUuid(userUuid);

        reservation = new Reservation(book, LocalDateTime.now());
        reservation.setUuid(reservationUuid);
        reservation.setUser(user);
    }

    @Test
    void givenSaveReservationShouldSaveSuccessfully() throws Exception {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setBookDTO(new BookDTO(bookUuid.toString(), book.getTitle(), book.getAuthor()));

        when(userService.hasReachedMaximumNumberOfReservations(user)).thenReturn(false);
        when(bookService.findBook(bookUuid)).thenReturn(book);
        when(bookService.reserveCopy(book)).thenReturn(book);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(userService.addReservation(any(User.class), any(Reservation.class))).thenReturn(user);

        ReservationDTO savedReservation = reservationService.saveReservation(reservationDTO, user);

        assertNotNull(savedReservation);
        assertEquals(bookUuid.toString(), savedReservation.getBookDTO().getUuid());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void givenSaveReservationWhenBookNotAvailableShouldThrowBookNotAvailableException() {
        book.setNumberOfAvailableCopies(0);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setBookDTO(new BookDTO(bookUuid.toString(), book.getTitle(), book.getAuthor()));

        when(bookService.findBook(bookUuid)).thenReturn(book);

        assertThrows(BookNotAvailableException.class, () -> reservationService.saveReservation(reservationDTO, user));
    }

    @Test
    void givenSaveReservationWhenUserExceededReservationsShouldThrowExceededNumberOfReservationsException() {
        when(userService.hasReachedMaximumNumberOfReservations(user)).thenReturn(true);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setBookDTO(new BookDTO(bookUuid.toString(), book.getTitle(), book.getAuthor()));

        assertThrows(ExceededNumberOfReservationsException.class, () -> reservationService.saveReservation(reservationDTO, user));
    }

    @Test
    void givenFindReservationShouldReturnReservation() {
        when(reservationRepository.getReferenceById(reservationUuid)).thenReturn(reservation);

        ReservationDTO result = reservationService.findReservation(reservationUuid);

        assertNotNull(result);
        assertEquals(reservationUuid.toString(), result.getUuid());
    }

    @Test
    void givenFindReservationWithInvalidUuidShouldThrowResourceNotFoundException() {
        when(reservationRepository.getReferenceById(reservationUuid)).thenThrow(EntityNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> reservationService.findReservation(reservationUuid));
    }

    @Test
    void givenCancelReservationShouldCancelSuccessfully() {
        when(reservationRepository.findById(reservationUuid)).thenReturn(Optional.of(reservation));
        when(bookService.findBook(bookUuid)).thenReturn(book);
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        reservationService.cancelReservation(reservationUuid);

        assertEquals(ReservationStatus.CANCELED, reservation.getReservationStatus());
        verify(bookService, times(1)).freeCopy(book);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void givenCancelReservationWithInvalidUuidShouldThrowResourceNotFoundException() {
        when(reservationRepository.findById(reservationUuid)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.cancelReservation(reservationUuid));
    }

    @Test
    void givenFindAllShouldReturnListOfReservations() {
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        List<ReservationDTO> reservations = reservationService.findAll();

        assertFalse(reservations.isEmpty());
        assertEquals(1, reservations.size());
    }

    @Test
    void givenFindByUserUuidShouldReturnUserReservations() {
        when(reservationRepository.findByUserUuid(userUuid)).thenReturn(List.of(reservation));

        List<ReservationDTO> reservations = reservationService.findByUserUuid(userUuid);

        assertFalse(reservations.isEmpty());
        assertEquals(1, reservations.size());
    }
}
