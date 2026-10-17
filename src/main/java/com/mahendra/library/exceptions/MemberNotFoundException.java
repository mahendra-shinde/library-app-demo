package com.mahendra.library.exceptions;

public class MemberNotFoundException extends RuntimeException {

	public MemberNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public MemberNotFoundException() {
		super("Member doesn't exists");
	}

}
