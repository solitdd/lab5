package org.example.exceptions;

public class DuplicateOrganizationException extends RuntimeException {
    public DuplicateOrganizationException(String message) {
        super(message);
    }
}
