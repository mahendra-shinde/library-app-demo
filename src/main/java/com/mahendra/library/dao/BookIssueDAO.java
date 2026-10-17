package com.mahendra.library.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mahendra.library.models.BookIssue;

import org.springframework.data.repository.CrudRepository;

public interface BookIssueDAO extends CrudRepository<BookIssue, Integer> {

	Optional<BookIssue> findById(Integer id);
	List<BookIssue> findByIssueDate(Date issueDate);
	List<BookIssue> findByEstimatedReturnDate(Date estimatedReturnDate);
	List<BookIssue> findByBookId(Integer bookId);
	List<BookIssue> findByMemberId(Integer memberId);
	List<BookIssue> findByStatus(char status);

}
