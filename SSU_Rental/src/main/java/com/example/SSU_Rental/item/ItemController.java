package com.example.SSU_Rental.item;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.member.AuthMember;
import com.example.SSU_Rental.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "아이템",description = "아이템 관련 API")
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "아이템 등록 요청",parameters={@Parameter(in = ParameterIn.HEADER,name = "X-AUTH_TOKEN",required = true,description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",description = "아이템 등록 성공", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @PostMapping("/items")
    public ResponseEntity<Long> register(@RequestBody ItemRequest itemRequest,
        @Parameter(hidden = true) @AuthMember Member member) {

        Long registerId = itemService.register(itemRequest, member.getId());
        return ResponseEntity.created(URI.create("/item/" + registerId)).body(registerId);

    }

    @Operation(summary = "대여 가능한 아이템 목록 요청")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "대여 가능한 아이템 목록 가져오기 성공", content = @Content(schema = @Schema(implementation = ResponsePageDTO.class)))
    })
    @GetMapping("/items")
    public ResponseEntity<ResponsePageDTO> getItemList(@ParameterObject RequestPageDTO requestPageDTO) {

        ResponsePageDTO responsePage = itemService.getItemList(requestPageDTO);
        return ResponseEntity.ok().body(responsePage);
    }

    @Operation(summary = "특정 아이템 정보 요청")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "특정 아이템 정보 가져오기 성공", content = @Content(schema = @Schema(implementation = ItemResponse.class)))
    })
    @GetMapping("/items/{item_id}")
    public ResponseEntity<ItemResponse> getOne(@Parameter(description = "아이템 ID",required = true) @PathVariable Long item_id){
        ItemResponse itemResponse = itemService.getOne(item_id);

        return ResponseEntity.ok(itemResponse);
    }

    @Operation(summary = "특정 아이템 정보 수정 요청",parameters={@Parameter(in = ParameterIn.HEADER,name = "X-AUTH_TOKEN",required = true,description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "특정 아이템 정보 수정  성공", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @PatchMapping("/items/{item_id}")
    public ResponseEntity<Long> modify(@Parameter(description = "아이템 ID",required = true) @PathVariable Long item_id,
        @RequestBody ItemRequest itemRequest,@Parameter(hidden = true) @AuthMember Member member) {

        itemService.modify(item_id, itemRequest, member.getId());
        return ResponseEntity.ok().body(item_id);

    }


    @Operation(summary = "특정 아이템 삭제 요청",parameters={@Parameter(in = ParameterIn.HEADER,name = "X-AUTH_TOKEN",required = true,description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "특정 아이템 삭제 성공")
    })
    @DeleteMapping("/items/{item_id}")
    public ResponseEntity delete(@Parameter(description = "아이템 ID",required = true) @PathVariable Long item_id,@Parameter(hidden = true) @AuthMember Member member) {
        itemService.delete(item_id, member.getId());
        return ResponseEntity.ok().build();
    }


}
