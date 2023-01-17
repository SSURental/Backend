package com.example.SSU_Rental.exception.notfound;

import com.example.SSU_Rental.exception.SsuRentalException;
import lombok.Getter;

@Getter
public class ItemNotFound extends SsuRentalException {

    private static final String MESSAGE = "존재하지 않는 아이템입니다.";

    public ItemNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
