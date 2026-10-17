package com.mahendra.library.exceptions;

public class BookNotFoundException extends RuntimeException {

	public BookNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public BookNotFoundException() {
		super("Book you were looking for doesn't exist in library yet !");
	}

}
