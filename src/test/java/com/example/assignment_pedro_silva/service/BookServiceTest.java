package com.example.assignment_pedro_silva.service;

import com.example.assignment_pedro_silva.model.Book;
import com.example.assignment_pedro_silva.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookService bookService;
    private Book book;
    private UUID bookId;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID();
        book = new Book("Anna Karenina", "Leo Tolstoy", "0075536323", 1);
        book.setUuid(bookId);
    }

    @Test
    void givenSaveBookShouldReturnSavedBook() {
        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookService.saveBook(book);

        assertNotNull(savedBook);
        assertEquals(book.getTitle(), savedBook.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void givenFindBookShouldReturnBookWhenBookExists() {
        when(bookRepository.getReferenceById(bookId)).thenReturn(book);

        Book foundBook = bookService.findBook(bookId);

        assertNotNull(foundBook);
        assertEquals(book.getTitle(), foundBook.getTitle());
        verify(bookRepository, times(1)).getReferenceById(bookId);
    }

    @Test
    void givenFindBookShouldThrowExceptionWhenBookNotFound() {
        when(bookRepository.getReferenceById(bookId)).thenThrow(new EntityNotFoundException());

        assertThrows(EntityNotFoundException.class, () -> bookService.findBook(bookId));
        verify(bookRepository, times(1)).getReferenceById(bookId);
    }

    @Test
    void givenFindAllShouldReturnListOfBooks() {
        List<Book> books = Arrays.asList(book, new Book("Madame Bovary", "Gustave Flaubert", "9789896411770", 2));
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> foundBooks = bookService.findAll();

        assertNotNull(foundBooks);
        assertEquals(2, foundBooks.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void givenReserveCopyShouldDecreaseAvailableCopiesAndIncreaseActiveReservations() {
        book.setNumberOfAvailableCopies(2);
        book.setNumberOfActiveReservations(0);

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book updatedBook = bookService.reserveCopy(book);

        assertEquals(1, updatedBook.getNumberOfAvailableCopies());
        assertEquals(1, updatedBook.getNumberOfActiveReservations());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void givenFreeCopyShouldIncreaseAvailableCopiesAndDecreaseActiveReservations() {
        book.setNumberOfAvailableCopies(0);
        book.setNumberOfActiveReservations(1);

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book updatedBook = bookService.freeCopy(book);

        assertEquals(1, updatedBook.getNumberOfAvailableCopies());
        assertEquals(0, updatedBook.getNumberOfActiveReservations());
        verify(bookRepository, times(1)).save(book);
    }
}
