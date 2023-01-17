package com.example.SSU_Rental.exception.notfound;

import com.example.SSU_Rental.exception.SsuRentalException;
import lombok.Getter;

@Getter
public class RatingNotFound extends SsuRentalException {
    private static final String MESSAGE = "존재하지 않는 리뷰입니다.";

    public RatingNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

}
