package com.example.SSU_Rental.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum Group {
    SCHOOL("SCHOOL"),
    STUDENT("STUDENT");

    String value;
    Group(String value) {this.value = value;}
    public String value() {return value;}
}
