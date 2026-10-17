package com.mahendra.library.rest;

import com.mahendra.library.dao.BookDAO;
import com.mahendra.library.exceptions.BookNotFoundException;
import com.mahendra.library.models.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(BookResource.class)
@DisplayName("BookResource REST Controller Tests")
class BookResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookDAO bookDAO;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book1;
    private Book book2;
    private List<Book> bookList;

    @BeforeEach
    void setUp() {
        book1 = new Book("Let Us C", "Yashwant Kanetkar", "Programming", 'A');
        book1.setId(1);

        book2 = new Book("Clean Code", "Robert C. Martin", "Programming", 'A');
        book2.setId(2);

        bookList = Arrays.asList(book1, book2);
    }

    @Test
    @DisplayName("Should return all books when books exist")
    void testFindAllWhenBooksExist() throws Exception {
        when(bookDAO.findAll()).thenReturn(bookList);

        mockMvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Let Us C"))
                .andExpect(jsonPath("$[0].author").value("Yashwant Kanetkar"))
                .andExpect(jsonPath("$[1].title").value("Clean Code"))
                .andExpect(jsonPath("$[1].author").value("Robert C. Martin"));
    }

    @Test
    @DisplayName("Should throw BookNotFoundException when no books exist")
    void testFindAllWhenNoBooksExist() throws Exception {
        when(bookDAO.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return book when book exists by ID")
    void testFindBookWhenBookExists() throws Exception {
        when(bookDAO.findById(1)).thenReturn(Optional.of(book1));

        mockMvc.perform(get("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Let Us C"))
                .andExpect(jsonPath("$.author").value("Yashwant Kanetkar"))
                .andExpect(jsonPath("$.category").value("Programming"))
                .andExpect(jsonPath("$.status").value("A"));
    }

    @Test
    @DisplayName("Should return 500 when DAO throws DataAccessException finding by ID")
    void testFindBookWhenDaoThrowsDataAccessException() throws Exception {
        when(bookDAO.findById(1)).thenThrow(new DataAccessException("DB error") {});
        mockMvc.perform(get("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should throw BookNotFoundException when book does not exist by ID")
    void testFindBookWhenBookDoesNotExist() throws Exception {
        when(bookDAO.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should find books by author fragment")
    void testFindByAuthorWhenMatchesExist() throws Exception {
        when(bookDAO.findByAuthorContainingIgnoreCase("Martin")).thenReturn(Arrays.asList(book2));

        mockMvc.perform(get("/api/books/author/Martin")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Clean Code"))
                .andExpect(jsonPath("$[0].author").value("Robert C. Martin"));
    }

    @Test
    @DisplayName("Should return 404 when no authors match search")
    void testFindByAuthorWhenNoMatches() throws Exception {
        when(bookDAO.findByAuthorContainingIgnoreCase("Unknown")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/books/author/Unknown")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should save new book successfully")
    void testSaveBook() throws Exception {
        Book newBook = new Book("Spring Boot in Action", "Craig Walls", "Programming", 'A');
        newBook.setId(3);

        when(bookDAO.save(any(Book.class))).thenReturn(newBook);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Spring Boot in Action"))
                .andExpect(jsonPath("$.author").value("Craig Walls"));
    }

    @Test
    @DisplayName("Should return 500 when DAO throws while saving")
    void testSaveBookWhenDaoThrows() throws Exception {
        Book newBook = new Book("Err Book", "Err Author", "Programming", 'A');
        when(bookDAO.save(any(Book.class))).thenThrow(new DataAccessException("save error") {});

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should update existing book successfully")
    void testUpdateExistingBook() throws Exception {
        Book updatedBook = new Book("Let Us Java", "Yashwant Kanetkar", "Programming", 'A');
        updatedBook.setId(1);

        when(bookDAO.findById(1)).thenReturn(Optional.of(book1));
        when(bookDAO.save(any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Let Us Java"));
    }

    @Test
    @DisplayName("Should return 500 when DAO throws while updating")
    void testUpdateWhenDaoThrowsOnSave() throws Exception {
        Book updatedBook = new Book("Let Us Java", "Yashwant Kanetkar", "Programming", 'A');
        updatedBook.setId(1);

        when(bookDAO.findById(1)).thenReturn(Optional.of(book1));
        when(bookDAO.save(any(Book.class))).thenThrow(new DataAccessException("update error") {});

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should throw BookNotFoundException when updating non-existent book")
    void testUpdateNonExistentBook() throws Exception {
        Book updatedBook = new Book("Non-existent Book", "Unknown Author", "Unknown", 'A');

        when(bookDAO.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/books/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete book successfully")
    void testDeleteBook() throws Exception {
        doNothing().when(bookDAO).deleteById(1);

        mockMvc.perform(delete("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("\"Successfully deleted a book\""));
    }

    @Test
    @DisplayName("Should return 500 when DAO throws while deleting")
    void testDeleteWhenDaoThrows() throws Exception {
        doThrow(new DataAccessException("delete error") {}).when(bookDAO).deleteById(1);

        mockMvc.perform(delete("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should handle malformed JSON in POST request")
    void testSaveBookWithMalformedJson() throws Exception {
        String malformedJson = "{ \"title\": \"Test Book\", \"author\": }";

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle missing required fields in request body")
    void testSaveBookWithMissingFields() throws Exception {
        String incompleteJson = "{ \"title\": \"Test Book\" }";

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(incompleteJson))
                .andDo(print())
                .andExpect(status().isCreated()); // Assuming the service handles null fields gracefully
    }

    @Test
    @DisplayName("Should return 500 when findAll DAO throws DataAccessException")
    void testFindAllWhenDaoThrowsException() throws Exception {
        when(bookDAO.findAll()).thenThrow(new DataAccessException("findAll error") {});

        mockMvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}
