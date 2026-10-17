package com.mahendra.library;

import com.mahendra.library.dao.BookDAO;
import com.mahendra.library.models.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@DisplayName("Library API Integration Tests")
class LibraryApiApplicationIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/books";
        
        // Clear database and add test data
        bookDAO.deleteAll();
        
        Book book1 = new Book("Let Us C", "Yashwant Kanetkar", "Programming", 'A');
        Book book2 = new Book("Clean Code", "Robert C. Martin", "Programming", 'A');
        Book book3 = new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 'N');
        
        bookDAO.save(book1);
        bookDAO.save(book2);
        bookDAO.save(book3);
    }

    @Test
    @DisplayName("Should retrieve all books from API")
    void testGetAllBooks() {
        ResponseEntity<Book[]> response = restTemplate.getForEntity(baseUrl, Book[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().length);
    }

    @Test
    @DisplayName("Should retrieve specific book by ID")
    void testGetBookById() {
        // First get all books to find a valid ID
        ResponseEntity<Book[]> allBooksResponse = restTemplate.getForEntity(baseUrl, Book[].class);
        Book[] books = allBooksResponse.getBody();
        Integer bookId = books[0].getId();
        
        ResponseEntity<Book> response = restTemplate.getForEntity(baseUrl + "/" + bookId, Book.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(bookId, response.getBody().getId());
    }

    @Test
    @DisplayName("Should return 404 for non-existent book")
    void testGetNonExistentBook() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/999", String.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should create new book via API")
    void testCreateNewBook() {
        Book newBook = new Book("Spring Boot in Action", "Craig Walls", "Programming", 'A');
        
        ResponseEntity<Book> response = restTemplate.postForEntity(baseUrl, newBook, Book.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Spring Boot in Action", response.getBody().getTitle());
        assertEquals("Craig Walls", response.getBody().getAuthor());
    }

    @Test
    @DisplayName("Should update existing book via API")
    void testUpdateExistingBook() {
        // First get a book to update
        ResponseEntity<Book[]> allBooksResponse = restTemplate.getForEntity(baseUrl, Book[].class);
        Book[] books = allBooksResponse.getBody();
        Book bookToUpdate = books[0];
        
        // Update the book
        bookToUpdate.setTitle("Updated Title");
        bookToUpdate.setAuthor("Updated Author");
        
        restTemplate.put(baseUrl + "/" + bookToUpdate.getId(), bookToUpdate);
        
        // Verify the update
        ResponseEntity<Book> updatedResponse = restTemplate.getForEntity(
            baseUrl + "/" + bookToUpdate.getId(), Book.class);
        
        assertEquals(HttpStatus.OK, updatedResponse.getStatusCode());
        assertEquals("Updated Title", updatedResponse.getBody().getTitle());
        assertEquals("Updated Author", updatedResponse.getBody().getAuthor());
    }

    @Test
    @DisplayName("Should delete book via API")
    void testDeleteBook() {
        // First get a book to delete
        ResponseEntity<Book[]> allBooksResponse = restTemplate.getForEntity(baseUrl, Book[].class);
        Book[] books = allBooksResponse.getBody();
        Integer bookIdToDelete = books[0].getId();
        
        // Delete the book
        restTemplate.delete(baseUrl + "/" + bookIdToDelete);
        
        // Verify deletion
        ResponseEntity<String> deletedResponse = restTemplate.getForEntity(
            baseUrl + "/" + bookIdToDelete, String.class);
        
        assertEquals(HttpStatus.NOT_FOUND, deletedResponse.getStatusCode());
    }

    @Test
    @DisplayName("Should handle application startup successfully")
    void contextLoads() {
        // This test ensures that the Spring context loads successfully
        assertNotNull(restTemplate);
        assertNotNull(bookDAO);
    }

    @Test
    @DisplayName("Should have database connection working")
    void testDatabaseConnection() {
        long count = bookDAO.count();
        assertTrue(count >= 0);
    }
}
