package com.darwgom.userapi.domain.exceptions;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(String email) {
        super("The email already registered: " + email);
    }
}
