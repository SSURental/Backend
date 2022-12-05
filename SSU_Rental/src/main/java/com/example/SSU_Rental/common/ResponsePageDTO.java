package com.example.SSU_Rental.common;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class ResponsePageDTO<DTO, EN> {

    @Schema(description = "페이지 내용")
    private List<DTO> contents;

    @Schema(description = "페이지 번호")
    private int page;

    @Schema(description = "페이지 사이즈")
    private int size;

    @Schema(description = "전체 페이지 크기")
    private int totalPage;

    @Schema(description = "다음 페이지 유무")
    private boolean hasNext;


    public ResponsePageDTO(Page<EN> result, Function<EN, DTO> fn) {
        contents = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();
        size = result.getPageable().getPageSize();
        page = result.getPageable().getPageNumber() + 1;
        hasNext = result.hasNext();
    }

}
