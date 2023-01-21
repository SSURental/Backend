package com.example.SSU_Rental.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends SsuRentalException{
    private static final String MESSAGE = "접근 권한이 없어 요청이 거부됐습니다.";

    public ForbiddenException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 403;
    }

}
