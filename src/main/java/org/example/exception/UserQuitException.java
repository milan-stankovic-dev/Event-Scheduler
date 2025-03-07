package org.example.exception;

public class UserQuitException extends RuntimeException {
    public UserQuitException(String message) {
        super(message);
    }
}
