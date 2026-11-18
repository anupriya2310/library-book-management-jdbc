
package com.example.libraryjdbcproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.libraryjdbcproject.model.Book;
import com.example.libraryjdbcproject.repository.BookRepository;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepository repository;

    @Autowired
    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    // (b) Add new book
    @PostMapping
    public ResponseEntity<String> addBook(
            @RequestBody Book book) {

        if (repository.existsById(book.getBookId())) {
            return ResponseEntity.badRequest()
                    .body("Book ID already exists");
        }

        if (book.getAvailableCopies() < 0) {
            return ResponseEntity.badRequest()
                    .body("Available copies cannot be negative");
        }

        repository.addBook(book);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Book added successfully");
    }

    // (c) Get all books
    @GetMapping
    public List<Book> getAllBooks() {
        return repository.getAllBooks();
    }

    // (c) Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(
            @PathVariable int id) {

        Book book = repository.getBookById(id);

        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found");
        }

        return ResponseEntity.ok(book);
    }

    // (d) Issue a book
    @PutMapping("/{id}/issue")
    public ResponseEntity<String> issueBook(
            @PathVariable int id) {

        Book book = repository.getBookById(id);

        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found");
        }

        if (book.getAvailableCopies() == 0) {
            return ResponseEntity.badRequest()
                    .body("Book not available");
        }

        repository.issueBook(id);

        return ResponseEntity.ok("Book issued successfully");
    }

    // (e) Delete book
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(
            @PathVariable int id) {

        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found");
        }

        repository.deleteBook(id);

        return ResponseEntity.ok("Book deleted successfully");
    }
}
