package com.mahendra.library.exceptions;

public class IssueNotFoundException extends RuntimeException {

	public IssueNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public IssueNotFoundException() {
		super("Issue you were looking for doesn't exist in library yet !");
	}

}
