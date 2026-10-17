package com.mahendra.library.rest;

import com.mahendra.library.dao.BookIssueDAO;
import com.mahendra.library.exceptions.IssueNotFoundException;
import com.mahendra.library.models.BookIssue;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/issues")
@Api(value="/api/issues", description="Operations for Book-Issues")
public class BookIssueResource {

	@Autowired private BookIssueDAO dao;
	
	@GetMapping(produces="application/json")
	@ApiOperation("Get all the Book Issues")
	public ResponseEntity<List<BookIssue>> findAll(){
		List<BookIssue> issues = new LinkedList<>();
		dao.findAll().forEach(issues::add);
		if(issues.isEmpty()){
			throw new IssueNotFoundException();
		}
		return new ResponseEntity<>(issues,HttpStatus.OK);
	}

	@GetMapping(produces="application/json",value="/{ID}")
	@ApiOperation("Search Issue by Issue ID")
	public ResponseEntity<BookIssue> findIssue(@PathVariable("ID") int issueId){
		Optional<BookIssue> issue = dao.findById(issueId);	
		if(issue.isEmpty()){
			throw new IssueNotFoundException();
		}
		return new ResponseEntity<>(issue.get(), HttpStatus.OK);
	}

	@PostMapping(consumes="application/json",produces="application/json")
	@ApiOperation("Issue a book to member")
	public ResponseEntity<BookIssue> save(BookIssue issue){
		BookIssue newIssue = dao.save(issue);
		return new ResponseEntity<>(newIssue,HttpStatus.CREATED);
	}

	@PutMapping(value="{ID}", produces="application/json", consumes="application/json")
	@ApiOperation("Updates an issue using Issue ID")
	public ResponseEntity<BookIssue> update(@PathVariable("ID") int issueId, BookIssue issue ){
		Optional<BookIssue> oldIssue = dao.findById(issueId);	
		if(oldIssue.isEmpty()){
			throw new IssueNotFoundException();
		}
		issue.setId(issueId);
		BookIssue newIssue = dao.save(issue);
		return new ResponseEntity<>(newIssue,HttpStatus.OK);
	}

	@DeleteMapping(value="{ID}", produces="application/json")
	@ApiOperation("Delete an Issue by Issue ID")
	public ResponseEntity<String> delete (@PathVariable("ID") int issueId){
		dao.deleteById(issueId);
		return new ResponseEntity<>("Member successfully deleted !",HttpStatus.OK);
	}
}
