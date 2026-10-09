package com.krishna.krishmart.exception;

/** Raised when an order cannot be fulfilled from current stock. */
public class InsufficientStockException extends AppException {
    /** Creates an inventory exception. */
    public InsufficientStockException(String message) { super(message); }
}