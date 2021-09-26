package com.novax.covidtrackerbackend.exceptions;

public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String message) {
        super(message);
    }
}
