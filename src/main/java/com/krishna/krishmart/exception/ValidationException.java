package com.krishna.krishmart.exception;

/** Raised when a request violates a business validation rule. */
public class ValidationException extends AppException {
    /** Creates a validation exception. */
    public ValidationException(String message) { super(message); }
}