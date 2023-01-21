package com.example.SSU_Rental.exception;

import lombok.Getter;

@Getter
public class ConflictException extends SsuRentalException {

    private static final String MESSAGE = "비즈니스 로직상 모순이 존재합니다.";

    public ConflictException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 409;
    }

}
