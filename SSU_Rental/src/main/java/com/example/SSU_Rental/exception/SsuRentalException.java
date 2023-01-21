package com.example.SSU_Rental.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public abstract class SsuRentalException extends RuntimeException{
    public final Map<String, String> validation = new HashMap<>();

    public SsuRentalException(String message) {
        super(message);
    }

    public SsuRentalException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
