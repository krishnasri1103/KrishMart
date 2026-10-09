package com.krishna.krishmart.util;

import com.krishna.krishmart.exception.ValidationException;
import java.math.BigDecimal;
import java.util.regex.Pattern;

/** Centralized validation for request values. */
public final class ValidationUtil {
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private ValidationUtil() {}
    /** Requires a nonblank value with a maximum length. */
    public static String required(String value, String field, int max) throws ValidationException {
        if (value == null || value.trim().isEmpty() || value.length() > max) throw new ValidationException(field + " is required and must be at most " + max + " characters.");
        return value.trim();
    }
    /** Validates a registration email. */
    public static String email(String value) throws ValidationException {
        String email = required(value, "Email", 255).toLowerCase();
        if (!EMAIL.matcher(email).matches()) throw new ValidationException("Enter a valid email address.");
        return email;
    }
    /** Validates a password without ever storing it. */
    public static void password(String value) throws ValidationException {
        if (value == null || value.length() < 8 || value.length() > 72) throw new ValidationException("Password must be 8 to 72 characters.");
    }
    /** Validates a positive-or-zero monetary value. */
    public static BigDecimal money(BigDecimal value) throws ValidationException {
        if (value == null || value.signum() < 0 || value.scale() > 2) throw new ValidationException("Price must be a non-negative amount with at most two decimals.");
        return value.setScale(2);
    }
}