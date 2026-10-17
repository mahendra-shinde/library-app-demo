package com.mahendra.library.dao;

import com.mahendra.library.models.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("BookDAO Repository Tests")
class BookDAOTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookDAO bookDAO;

    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        // Create test data
        book1 = new Book("Let Us C", "Yashwant Kanetkar", "Programming", 'A');
        book2 = new Book("Clean Code", "Robert C. Martin", "Programming", 'A');
        book3 = new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 'N');

        // Persist test data
        entityManager.persistAndFlush(book1);
        entityManager.persistAndFlush(book2);
        entityManager.persistAndFlush(book3);
    }

    @Test
    @DisplayName("Should find book by ID when book exists")
    void testFindByIdWhenBookExists() {
        Optional<Book> foundBook = bookDAO.findById(book1.getId());
        
        assertTrue(foundBook.isPresent());
        assertEquals("Let Us C", foundBook.get().getTitle());
        assertEquals("Yashwant Kanetkar", foundBook.get().getAuthor());
    }

    @Test
    @DisplayName("Should return empty optional when book ID does not exist")
    void testFindByIdWhenBookDoesNotExist() {
        Optional<Book> foundBook = bookDAO.findById(999);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    @DisplayName("Should find books by author")
    void testFindByAuthor() {
        List<Book> books = bookDAO.findByAuthor("Yashwant Kanetkar");
        
        assertEquals(1, books.size());
        assertEquals("Let Us C", books.get(0).getTitle());
    }

    @Test
    @DisplayName("Should find books by category")
    void testFindByCategory() {
        List<Book> programmingBooks = bookDAO.findByCategory("Programming");
        
        assertEquals(2, programmingBooks.size());
        assertTrue(programmingBooks.stream().anyMatch(book -> book.getTitle().equals("Let Us C")));
        assertTrue(programmingBooks.stream().anyMatch(book -> book.getTitle().equals("Clean Code")));
    }

    @Test
    @DisplayName("Should find books by title")
    void testFindByTitle() {
        List<Book> books = bookDAO.findByTitle("Clean Code");
        
        assertEquals(1, books.size());
        assertEquals("Robert C. Martin", books.get(0).getAuthor());
    }

    @Test
    @DisplayName("Should find available books by title")
    void testFindAvailableByTitle() {
        List<Book> availableBooks = bookDAO.findAvailableByTitle("Let Us C");
        
        assertEquals(1, availableBooks.size());
        assertEquals('A', availableBooks.get(0).getStatus());
    }

    @Test
    @DisplayName("Should not find unavailable books when searching available by title")
    void testFindAvailableByTitleWhenUnavailable() {
        List<Book> availableBooks = bookDAO.findAvailableByTitle("The Great Gatsby");
        
        assertEquals(0, availableBooks.size());
    }

    @Test
    @DisplayName("Should find available books by author")
    void testFindAvailableByAuthor() {
        List<Book> availableBooks = bookDAO.findAvailableByAuthor("Robert C. Martin");
        
        assertEquals(1, availableBooks.size());
        assertEquals("Clean Code", availableBooks.get(0).getTitle());
    }

    @Test
    @DisplayName("Should find available books by category")
    void testFindAvailableByCategory() {
        List<Book> availableProgrammingBooks = bookDAO.findAvailableByCategory("Programming");
        
        assertEquals(2, availableProgrammingBooks.size());
        assertTrue(availableProgrammingBooks.stream().allMatch(book -> book.getStatus() == 'A'));
    }

    @Test
    @DisplayName("Should not find available books in category when all are unavailable")
    void testFindAvailableByCategoryWhenAllUnavailable() {
        List<Book> availableFictionBooks = bookDAO.findAvailableByCategory("Fiction");
        
        assertEquals(0, availableFictionBooks.size());
    }

    @Test
    @DisplayName("Should save and retrieve book")
    void testSaveAndRetrieveBook() {
        Book newBook = new Book("Spring in Action", "Craig Walls", "Programming", 'A');
        
        Book savedBook = bookDAO.save(newBook);
        
        assertNotNull(savedBook.getId());
        
        Optional<Book> retrievedBook = bookDAO.findById(savedBook.getId());
        assertTrue(retrievedBook.isPresent());
        assertEquals("Spring in Action", retrievedBook.get().getTitle());
    }

    @Test
    @DisplayName("Should delete book")
    void testDeleteBook() {
        Integer bookId = book1.getId();
        
        bookDAO.deleteById(bookId);
        
        Optional<Book> deletedBook = bookDAO.findById(bookId);
        assertFalse(deletedBook.isPresent());
    }
}
