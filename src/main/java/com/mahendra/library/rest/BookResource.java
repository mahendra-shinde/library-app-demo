package com.mahendra.library.rest;

import com.mahendra.library.dao.BookDAO;
import com.mahendra.library.exceptions.BookNotFoundException;
import com.mahendra.library.models.Book;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataAccessException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * REST controller exposing endpoints to manage Book resources.
 *
 * <p>Provides JSON endpoints to list all books, find a book by id, search by author,
 * create, update and delete books. Typical HTTP status codes are used:
 * 200 (OK), 201 (CREATED), 404 (NOT FOUND) via BookNotFoundException and
 * 500 (INTERNAL SERVER ERROR) for data access problems.</p>
 */
@RestController
@RequestMapping("/api/books")
@Api(value="/api/books", description="Operations for Books")
public class BookResource {

	@Autowired private BookDAO dao;
	
	/**
	 * List all books in the library.
	 *
	 * @return ResponseEntity containing the list of books and HTTP 200 (OK).
	 * @throws BookNotFoundException if no books are present in the library.
	 */
	@GetMapping(produces = "application/json")
	@ApiOperation("List all books from library")
	public ResponseEntity<List<Book>> findAll(){
		List<Book> books = new ArrayList<>();
		dao.findAll().forEach(books::add);
		if(books.isEmpty()){
			throw new BookNotFoundException();
		}
		//Write a test to verify that the method returns a list of books when books exist

		return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	}

	/**
	 * Find a single book by its numeric identifier.
	 *
	 * @param id the identifier of the book to retrieve
	 * @return ResponseEntity containing the found Book and HTTP 200 (OK)
	 * @throws BookNotFoundException if no book is found with the given id
	 * @throws org.springframework.dao.DataAccessException if a data access error occurs
	 */
	@GetMapping(produces = "application/json",value = "/{ID}")
	@ApiOperation("Find book by Book ID")
	public ResponseEntity<Book> findBook(@PathVariable("ID") Integer id){
		try {
			Optional<Book> book = dao.findById(id);
			if(book.isEmpty()){
				throw new BookNotFoundException();
			}
			System.out.println("Book found: "+book.get().getTitle());
			return new ResponseEntity<Book>(book.get(), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Find books whose author name contains the given text (case-insensitive).
	 *
	 * @param authorName the author name or fragment to search for
	 * @return ResponseEntity containing the list of matching books and HTTP 200 (OK)
	 * @throws BookNotFoundException if no books match the provided author name
	 */
	@GetMapping(produces = "application/json", value = "/author/{authorName}")
	@ApiOperation("Find books by Author name")
	public ResponseEntity<List<Book>> findByAuthor(@PathVariable String authorName){
		List<Book> books = dao.findByAuthorContainingIgnoreCase(authorName);
		if(books.isEmpty()){
			throw new BookNotFoundException();
		}
		return ResponseEntity.ok(books);
	}


	/**
	 * Save a new book to the library.
	 *
	 * @param book the Book object to create (expected as JSON in the request body)
	 * @return ResponseEntity containing the created Book and HTTP 201 (CREATED)
	 */
	@PostMapping(produces="application/json", consumes="application/json")
	@ApiOperation("Save new book")
	public ResponseEntity<Book> save(Book book){
		Book newBook = dao.save(book);
		return new ResponseEntity<>(newBook, HttpStatus.CREATED);
	}

	/**
	 * Update an existing book identified by its id.
	 *
	 * @param bookId the id of the book to update
	 * @param book the Book object containing updated values (expected as JSON in the request body)
	 * @return ResponseEntity containing the updated Book and HTTP 200 (OK)
	 * @throws BookNotFoundException if the book to update does not exist
	 */
	@PutMapping(produces = "application/json", consumes="application/json", value = "/{ID}")
	public ResponseEntity<Book> update(@PathVariable("ID") int bookId, Book book){
		Optional<Book> oldBook = dao.findById(bookId);
		if(oldBook.isEmpty()){
			throw new BookNotFoundException();
		}
		Book newBook = dao.save(book);
		return new ResponseEntity<>(newBook, HttpStatus.OK);
	}

	/**
	 * Delete a book by its id.
	 *
	 * @param bookId the id of the book to delete
	 * @return ResponseEntity with a success message and HTTP 200 (OK)
	 */
	@DeleteMapping(value = "/{ID}", produces = "application/json")
	@ApiOperation("Delete a book by id")
	public ResponseEntity<String> delete(@PathVariable("ID") int bookId){
		dao.deleteById(bookId);
		return new ResponseEntity<>("Successfully deleted a book",HttpStatus.OK);
	}
}
