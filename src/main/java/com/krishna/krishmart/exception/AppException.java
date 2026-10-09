package com.krishna.krishmart.exception;

/** Base checked exception for expected application failures. */
public class AppException extends Exception {
    /** Creates an application exception with a user-safe message. */
    public AppException(String message) { super(message); }
    /** Creates an application exception with a cause. */
    public AppException(String message, Throwable cause) { super(message, cause); }
}