package com.example.SSU_Rental.exception.notfound;

import com.example.SSU_Rental.exception.SsuRentalException;
import lombok.Getter;

@Getter
public class MemberNotFound extends SsuRentalException {

    private static final String MESSAGE = "존재하지 않는 멤버입니다.";

    public MemberNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
