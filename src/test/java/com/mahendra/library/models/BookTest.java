package com.mahendra.library.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Book Model Tests")
class BookTest {

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
    }

    @Test
    @DisplayName("Should create book with default constructor")
    void testDefaultConstructor() {
        assertNotNull(book);
        assertNull(book.getId());
        assertNull(book.getTitle());
        assertNull(book.getAuthor());
        assertNull(book.getCategory());
        assertEquals(0, book.getStatus()); // default char value
    }

    @Test
    @DisplayName("Should create book with parameterized constructor")
    void testParameterizedConstructor() {
        Book paramBook = new Book("Let Us C", "Yashwant Kanetkar", "Programming", 'A');
        
        assertNotNull(paramBook);
        assertEquals("Let Us C", paramBook.getTitle());
        assertEquals("Yashwant Kanetkar", paramBook.getAuthor());
        assertEquals("Programming", paramBook.getCategory());
        assertEquals('A', paramBook.getStatus());
    }

    @Test
    @DisplayName("Should set and get book ID correctly")
    void testIdGetterSetter() {
        Integer expectedId = 101;
        book.setId(expectedId);
        assertEquals(expectedId, book.getId());
    }

    @Test
    @DisplayName("Should set and get book title correctly")
    void testTitleGetterSetter() {
        String expectedTitle = "Clean Code";
        book.setTitle(expectedTitle);
        assertEquals(expectedTitle, book.getTitle());
    }

    @Test
    @DisplayName("Should set and get book author correctly")
    void testAuthorGetterSetter() {
        String expectedAuthor = "Robert C. Martin";
        book.setAuthor(expectedAuthor);
        assertEquals(expectedAuthor, book.getAuthor());
    }

    @Test
    @DisplayName("Should set and get book category correctly")
    void testCategoryGetterSetter() {
        String expectedCategory = "Software Engineering";
        book.setCategory(expectedCategory);
        assertEquals(expectedCategory, book.getCategory());
    }

    @Test
    @DisplayName("Should set and get book status correctly")
    void testStatusGetterSetter() {
        char expectedStatus = 'N';
        book.setStatus(expectedStatus);
        assertEquals(expectedStatus, book.getStatus());
    }

    @Test
    @DisplayName("Should handle available status")
    void testAvailableStatus() {
        book.setStatus('A');
        assertEquals('A', book.getStatus());
    }

    @Test
    @DisplayName("Should handle not available status")
    void testNotAvailableStatus() {
        book.setStatus('N');
        assertEquals('N', book.getStatus());
    }

    @Test
    @DisplayName("Should handle null title gracefully")
    void testNullTitle() {
        book.setTitle(null);
        assertNull(book.getTitle());
    }

    @Test
    @DisplayName("Should handle empty title")
    void testEmptyTitle() {
        book.setTitle("");
        assertEquals("", book.getTitle());
    }
}
