package com.example.SSU_Rental.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends SsuRentalException{
    private static final String MESSAGE = "클라이언트가 인증되지 않았거나, 유효한 인증 정보가 부족하여 요청이 거부되었습니다.";

    public UnauthorizedException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }

}
