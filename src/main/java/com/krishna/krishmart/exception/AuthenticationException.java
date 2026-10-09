package com.krishna.krishmart.exception;

/** Raised when credentials are invalid. */
public class AuthenticationException extends AppException {
    /** Creates an authentication exception. */
    public AuthenticationException(String message) { super(message); }
}