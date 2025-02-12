package com.example.assignment_pedro_silva.service;

import com.example.assignment_pedro_silva.exceptions.ResourceNotFoundException;
import com.example.assignment_pedro_silva.model.Book;
import com.example.assignment_pedro_silva.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public Book findBook(UUID uuid) {
        return bookRepository.getReferenceById(uuid);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book reserveCopy(Book book) {
        book.setNumberOfAvailableCopies(book.getNumberOfAvailableCopies() - 1);
        book.setNumberOfActiveReservations(book.getNumberOfActiveReservations() + 1);
        return saveBook(book);
    }

    public Book freeCopy(Book book) {
        book.setNumberOfActiveReservations(book.getNumberOfActiveReservations() - 1);
        book.setNumberOfAvailableCopies(book.getNumberOfAvailableCopies() + 1);
        return saveBook(book);
    }

    public List<Book> generateBooks() {
        populateBooksTable();
        return findAll();
    }

    private void populateBooksTable() {
        Book book1 = new Book("Anna Karenina", "Leo Tolstoy", "0075536323", 1);
        saveBook(book1);
        Book book2 = new Book("Madame Bovary", "Gustave Flaubert", "9789896411770", 2);
        saveBook(book2);
        Book book3 = new Book("War and peace", "Leo Tolstoy", "9781853260629", 2);
        saveBook(book3);
        Book book4 = new Book("The great Gatsby", "F. Scott Fitzgerald", "9780593201060", 3);
        saveBook(book4);
        Book book5 = new Book("Lolita", "Vladimir Nabokov", "9789896413606", 1);
        saveBook(book5);
    }
}
