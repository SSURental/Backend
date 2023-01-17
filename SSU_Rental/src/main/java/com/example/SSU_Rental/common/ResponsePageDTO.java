package com.example.SSU_Rental.common;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class ResponsePageDTO<DTO, EN> {

    private List<DTO> contents;

    private int page;

    private int size;

    private int totalPage;

    private boolean hasNext;


    public ResponsePageDTO(Page<EN> result, Function<EN, DTO> fn) {
        contents = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();
        size = result.getPageable().getPageSize();
        page = result.getPageable().getPageNumber()+1;
        hasNext = result.hasNext();
    }

    public List<DTO> getContents() {
            return contents;
    }
}
