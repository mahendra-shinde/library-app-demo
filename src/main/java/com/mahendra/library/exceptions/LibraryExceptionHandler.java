package com.mahendra.library.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LibraryExceptionHandler  {
	
	@ExceptionHandler({BookNotFoundException.class, MemberNotFoundException.class, IssueNotFoundException.class })
	public ResponseEntity<Map<String,String>> showError(RuntimeException ex){
		Map<String,String> result = new HashMap<>();
		result.put("Error", ex.getMessage());
		if(ex.getCause() != null )
		{
			result.put("Cause-by: ", ex.getCause().getMessage());
			result.put("Exception: ", ex.getCause().getClass().getName());
		}
		return new ResponseEntity<Map<String,String>>(result, HttpStatus.NOT_FOUND);
	}
}
