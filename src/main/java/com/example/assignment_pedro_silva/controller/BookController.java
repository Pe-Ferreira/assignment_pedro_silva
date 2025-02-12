package com.example.assignment_pedro_silva.controller;

import com.example.assignment_pedro_silva.model.Book;
import com.example.assignment_pedro_silva.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "Book management")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Get a list with all books")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    public ResponseEntity<List<Book>> getAll() {
        List<Book> books = this.bookService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @PostMapping
    @Operation(summary = "Create test books", description = "Creates a list of five books to serve as test data for the reservation's endpoints")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Books created successfully")
    public ResponseEntity<List<Book>> generateBooks() {
        this.bookService.generateBooks();
        List<Book> books = this.bookService.findAll();
        return ResponseEntity.status(HttpStatus.CREATED).body(books);
    }
}
