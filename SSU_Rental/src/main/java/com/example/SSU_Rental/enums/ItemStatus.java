package com.example.SSU_Rental.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor //모든 필드 값을 파라미터로 받는 생성자를 만듦
public enum ItemStatus {
    AVAILABLE,
    RESERVATION,
    LOAN,
    ETC;    //대여가능, 예약중, 대여중, 예외의상황
}