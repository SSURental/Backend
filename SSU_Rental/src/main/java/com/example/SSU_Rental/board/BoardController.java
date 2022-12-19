package com.example.SSU_Rental.board;

import com.example.SSU_Rental.config.LogExecutionTime;
import com.example.SSU_Rental.exception.ErrorResponseDTO;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "게시글", description = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시글 작성 요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "게시글 작성 성공", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @PostMapping("/boards")
    @LogExecutionTime
    public ResponseEntity<Long> register(@Validated @RequestBody BoardRequest boardRequest,
        @Parameter(hidden = true) @AuthMember Member member) {

        Long registerId = boardService.register(boardRequest, member.getId());
        return ResponseEntity.created(URI.create("/board/" + registerId)).body(registerId);

    }

    @Operation(summary = "게시글 좋아요 요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "게시글 좋아요", content = @Content(schema = @Schema(implementation = Long.class))),
        @ApiResponse(responseCode = "404",description = "게시글 아이디가 잘못되었습니다",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/boards/{boardId}/like")
    @LogExecutionTime
    public ResponseEntity<Long> like(
        @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId) {
        boardService.like(boardId);
        return ResponseEntity.ok().body(boardId);
    }

    @Operation(summary = "게시글 싫어요 요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "게시글 추천", content = @Content(schema = @Schema(implementation = Long.class))),
        @ApiResponse(responseCode = "404",description = "게시글 아이디가 잘못되었습니다",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/boards/{boardId}/dislike")
    @LogExecutionTime
    public ResponseEntity<Long> dislike(
        @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId) {
        boardService.dislike(boardId);
        return ResponseEntity.ok().body(boardId);
    }

    @Operation(summary = "게시글 경고 요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "게시글 신고", content = @Content(schema = @Schema(implementation = Long.class))),
        @ApiResponse(responseCode = "404",description = "게시글 아이디가 잘못되었습니다",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/boards/{boardId}/warn")
    @LogExecutionTime
    public ResponseEntity<Long> warn(
        @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId) {
        boardService.warn(boardId);
        return ResponseEntity.ok().body(boardId);
    }

    @Operation(summary = "게시글 하나 요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "게시글 하나 가져오기", content = @Content(schema = @Schema(implementation = BoardResponse.class))),
        @ApiResponse(responseCode = "404",description = "게시글 아이디가 잘못되었습니다",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/boards/{boardId}")
    @LogExecutionTime
    public ResponseEntity<BoardResponse> getOne(
        @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId) {

        return ResponseEntity.ok().body(boardService.getOne(boardId));
    }

    /**
     * /boards ? page =1& size=5& sort=id -> 최신 순대로 정렬 /boards ? page =1& size=5& sort=see_cnt ->
     * 조회수 대로 정렬
     *
     * @param requestPageDTO
     * @return
     */

    @Operation(summary = "게시글 목록 요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "게시글 목록 가져오기", content = @Content(schema = @Schema(implementation = ResponsePageDTO.class))),
        @ApiResponse(responseCode = "404",description = "게시글 아이디가 잘못되었습니다",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @LogExecutionTime
    @GetMapping("/boards")
    public ResponseEntity<ResponsePageDTO> getList(@ParameterObject RequestPageDTO requestPageDTO) {
        ResponsePageDTO responsePage = boardService.getBoardList(requestPageDTO);

        return ResponseEntity.ok().body(responsePage);
    }

//    @Operation(summary = "조회 수 높은 순 게시글 목록 요청")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200",description = "인기 게시글 목록 가져오기", content = @Content(schema = @Schema(implementation = ResponsePageDTO.class)))
//    })
//    @GetMapping("/boards/ranking")
//    public ResponseEntity<ResponsePageDTO> getPopularList(@ParameterObject RequestPageDTO requestPageDTO) {
//        ResponsePageDTO responsePage = boardService.getPopularList(requestPageDTO);
//
//        return ResponseEntity.ok().body(responsePage);
//    }

//    사용하지 않은 기능 삭제
//    @Operation(summary = "게시글 수정 요청",parameters={@Parameter(in = ParameterIn.HEADER,name = "X-AUTH_TOKEN",required = true,description = "발급받은 JWT토큰")})
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200",description = "게시글 수정 ", content = @Content(schema = @Schema(implementation = Long.class)))
//    })
//    @PatchMapping("/boards/{boardId}")
//    public ResponseEntity<Long> modify(@Parameter(description = "게시글 ID",required = true) @PathVariable Long boardId,
//        @RequestBody BoardRequest boardRequest,@Parameter(hidden = true)  @AuthMember Member member) {
//
//        boardService.modify(boardId, boardRequest, member.getId());
//        return ResponseEntity.ok().body(boardId);
//    }

    @Operation(summary = "게시글 삭제 요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH_TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "게시글 삭제"),
        @ApiResponse(responseCode = "404",description = "게시글 아이디가 잘못되었습니다",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "403",description = "접근 권한이 없습니다.",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @LogExecutionTime
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity delete(
        @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId,
        @Parameter(hidden = true) @AuthMember Member member) {
        boardService.delete(boardId, member.getId());
        return ResponseEntity.ok().build();
    }
}
