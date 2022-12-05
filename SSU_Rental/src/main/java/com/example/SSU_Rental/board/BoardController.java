package com.example.SSU_Rental.board;

import com.example.SSU_Rental.member.AuthMember;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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

@Tag(name = "게시글",description = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시글 작성 요청",parameters={@Parameter(in = ParameterIn.HEADER,name = "X-AUTH_TOKEN",required = true,description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",description = "게시글 작성 성공", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @PostMapping("/board")
    public ResponseEntity<Long> register(@RequestBody BoardRequest boardRequest,
        @Parameter(hidden = true) @AuthMember Member member) {

        Long registerId = boardService.register(boardRequest, member.getId());
        return ResponseEntity.created(URI.create("/board/" + registerId)).body(registerId);

    }

    @Operation(summary = "게시글 추천 요청",parameters={@Parameter(in = ParameterIn.HEADER,name = "X-AUTH_TOKEN",required = true,description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "게시글 추천", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @PostMapping("/board/{board_id}/recommend")
    public ResponseEntity<Long> recommend(@Parameter(description = "게시글 ID",required = true) @PathVariable Long board_id) {
        boardService.recommend(board_id);
        return ResponseEntity.ok().body(board_id);
    }

    @Operation(summary = "게시글 경고 요청",parameters={@Parameter(in = ParameterIn.HEADER,name = "X-AUTH_TOKEN",required = true,description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "게시글 신고", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @PostMapping("/board/{board_id}/warn")
    public ResponseEntity<Long> warn(@Parameter(description = "게시글 ID",required = true)  @PathVariable Long board_id) {
        boardService.warn(board_id);
        return ResponseEntity.ok().body(board_id);
    }

    @Operation(summary = "게시글 하나 요청")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "게시글 하나 가져오기", content = @Content(schema = @Schema(implementation = BoardResponse.class)))
    })
    @GetMapping("/board/{board_id}")
    public ResponseEntity<BoardResponse> getOne(@Parameter(description = "게시글 ID",required = true) @PathVariable Long board_id) {

        return ResponseEntity.ok().body(boardService.getOne(board_id));
    }


    @Operation(summary = "게시글 목록 요청")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "게시글 목록 가져오기", content = @Content(schema = @Schema(implementation = ResponsePageDTO.class)))
    })
    @GetMapping("/board")
    public ResponseEntity<ResponsePageDTO> getList(@ParameterObject RequestPageDTO requestPageDTO) {
        ResponsePageDTO responsePage = boardService.getList(requestPageDTO);

        return ResponseEntity.ok().body(responsePage);
    }


    @Operation(summary = "게시글 수정 요청",parameters={@Parameter(in = ParameterIn.HEADER,name = "X-AUTH_TOKEN",required = true,description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "게시글 수정 ", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @PatchMapping("/board/{board_id}")
    public ResponseEntity<Long> modify(@Parameter(description = "게시글 ID",required = true) @PathVariable Long board_id,
        @RequestBody BoardRequest boardRequest,@Parameter(hidden = true)  @AuthMember Member member) {

        boardService.modify(board_id, boardRequest, member.getId());
        return ResponseEntity.ok().body(board_id);
    }

    @Operation(summary = "게시글 삭제 요청",parameters={@Parameter(in = ParameterIn.HEADER,name = "X-AUTH_TOKEN",required = true,description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "게시글 삭제")
    })
    @DeleteMapping("/board/{board_id}")
    public ResponseEntity delete(@Parameter(description = "게시글 ID",required = true) @PathVariable Long board_id, @Parameter(hidden = true)   @AuthMember Member member) {
        boardService.delete(board_id, member.getId());
        return ResponseEntity.ok().build();
    }
}
