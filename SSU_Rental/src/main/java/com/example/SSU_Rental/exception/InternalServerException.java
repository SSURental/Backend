package com.example.SSU_Rental.exception;

import lombok.Getter;

@Getter
public class InternalServerException extends SsuRentalException{
    private static final String MESSAGE = "서버 오류입니다.";

    public InternalServerException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}
