package com.krishna.krishmart.exception;

/** Raised when a requested entity does not exist. */
public class NotFoundException extends AppException {
    /** Creates a not-found exception. */
    public NotFoundException(String message) { super(message); }
}