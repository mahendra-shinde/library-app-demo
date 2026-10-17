package com.mahendra.library.dao;

import java.util.List;
import java.util.Optional;

import com.mahendra.library.models.Book;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BookDAO extends CrudRepository<Book, Integer >{


	Optional<Book> findById(Integer id);

	List<Book> findByAuthor(String author);
	List<Book> findByCategory(String category);
	List<Book> findByTitle(String title);

	@Query("SELECT b FROM Book b WHERE b.status = 'A' and b.title=:title")
	List<Book> findAvailableByTitle(String title);

	@Query("SELECT b FROM Book b WHERE b.status = 'A' and b.author=:author")
	List<Book> findAvailableByAuthor(String author);

	@Query("SELECT b FROM Book b WHERE b.status = 'A' and b.category=:category")
	List<Book> findAvailableByCategory(String category);

	// findByAuthorContainingIgnoreCase
	// JPQSL Query to find books where author name contains the given string (case insensitive)
	@Query("SELECT b FROM Book b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :authorName, '%'))")
	List<Book> findByAuthorContainingIgnoreCase(String authorName);
}
