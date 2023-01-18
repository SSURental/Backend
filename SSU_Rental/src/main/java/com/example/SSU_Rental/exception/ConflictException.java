package com.example.SSU_Rental.exception;

import lombok.Getter;

@Getter
public class ConflictException extends SsuRentalException {

    private static final String MESSAGE = "이미 삭제 되었습니다.";

    public ConflictException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 409;
    }

}
