package com.example.SSU_Rental.common;

import static java.lang.Math.max;
import static java.lang.Math.min;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPageDTO {


    private final static Integer MAX_SIZE = 500;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    @Enum(enumClass = Group.class, ignoreCase = true,message = "Group에는 STUDENT, SCHOOL만 들어갈 수 있습니다.")
    private String group;

    private String sort;

    public Pageable getPageable(){
        return PageRequest.of(page-1,size);
    }


    public long getOffset(){
        return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
    }

}
