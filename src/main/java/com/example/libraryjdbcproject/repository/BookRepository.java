
package com.example.libraryjdbcproject.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.libraryjdbcproject.model.Book;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Convert database row into Book object
    private RowMapper<Book> rowMapper = (rs, rowNum) -> {

        Book book = new Book();

        book.setBookId(rs.getInt("book_id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setGenre(rs.getString("genre"));
        book.setAvailableCopies(
                rs.getInt("available_copies"));

        return book;
    };

    // Add book
    public int addBook(Book book) {

        String sql = "INSERT INTO books VALUES (?, ?, ?, ?, ?)";

        return jdbcTemplate.update(sql,
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getAvailableCopies());
    }

    // Check book ID
    public boolean existsById(int id) {

        String sql = "SELECT COUNT(*) FROM books WHERE book_id = ?";

        Integer count = jdbcTemplate.queryForObject(
                sql, Integer.class, id);

        return count != null && count > 0;
    }

    // Get all books
    public List<Book> getAllBooks() {

        String sql = "SELECT * FROM books";

        return jdbcTemplate.query(sql, rowMapper);
    }

    // Get book by ID
    public Book getBookById(int id) {

        String sql = "SELECT * FROM books WHERE book_id = ?";

        List<Book> list = jdbcTemplate.query(
                sql, rowMapper, id);

        return list.isEmpty() ? null : list.get(0);
    }

    // Issue book
    public int issueBook(int id) {

        String sql = "UPDATE books SET available_copies = "
                   + "available_copies - 1 "
                   + "WHERE book_id = ? AND available_copies > 0";

        return jdbcTemplate.update(sql, id);
    }

    // Delete book
    public int deleteBook(int id) {

        String sql = "DELETE FROM books WHERE book_id = ?";

        return jdbcTemplate.update(sql, id);
    }
}
