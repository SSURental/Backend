package com.example.SSU_Rental.exception;

import lombok.Getter;

@Getter
public class AlreadyDeletedException extends SsuRentalException{
    private static final String MESSAGE = "이미 삭제 되었습니다.";

    public AlreadyDeletedException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 410;
    }

}
