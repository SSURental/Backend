package com.example.SSU_Rental.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends SsuRentalException{
    private static final String MESSAGE = "잘못된 요청입니다.";

    public BadRequestException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
