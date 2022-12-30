package com.example.SSU_Rental.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestPageDTO {


    private int page;

    private int size;

    private Group group;

    private String sort;


    public Pageable getPageable(){
        if(sort==null){
            return PageRequest.of(this.page-1,this.size);
        }else {
            return PageRequest.of(this.page-1,this.size, Sort.by(this.sort).descending());
        }
    }
}
