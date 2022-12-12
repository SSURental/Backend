package com.example.SSU_Rental.boardrp;

import com.example.SSU_Rental.exception.ErrorResponseDTO;
import com.example.SSU_Rental.member.AuthMember;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
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


@Tag(name = "게시글 댓글", description = "게시글 댓글 관련 API")
@RestController
@RequiredArgsConstructor
public class BoardrpController {


    private final BoardrpService boardrpService;


    @Operation(summary = "게시글 관련 댓글 작성 요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH_TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "게시글 관련 댓글 작성 성공", content = @Content(schema = @Schema(implementation = Long.class))),
        @ApiResponse(responseCode = "404", description = "게시글 아이디가 잘못되었습니다", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/boards/{boardId}/replys")
    public ResponseEntity<Long> register(
        @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId,
        @Validated @RequestBody BoardrpRequest boardrpRequest,
        @Parameter(hidden = true) @AuthMember Member member) {
        Long registerId = boardrpService.register(boardId, boardrpRequest, member.getId());
        return ResponseEntity.created(URI.create("/board/" + boardId + "/reply" + registerId))
            .body(registerId);
    }

    @Operation(summary = "게시글 관련 댓글 목록 요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH_TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "게시글 관련 댓글 목록 가져오기", content = @Content(schema = @Schema(implementation = ResponsePageDTO.class))),
        @ApiResponse(responseCode = "404", description = "게시글 아이디가 잘못되었습니다", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/boards/{boardId}/replys")
    public ResponseEntity<ResponsePageDTO> getList(
        @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId,
        @ParameterObject RequestPageDTO requestPageDTO) {
        ResponsePageDTO responsePage = boardrpService.getReplyList(boardId, requestPageDTO);
        return ResponseEntity.ok().body(responsePage);
    }

//    사용하지 않은 기능 삭제
//    @Operation(summary = "게시글 관련 댓글 수정 요청",parameters={@Parameter(in = ParameterIn.HEADER,name = "X-AUTH_TOKEN",required = true,description = "발급받은 JWT토큰")})
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200",description = "게시글 관련 댓글 수정 요청", content = @Content(schema = @Schema(implementation = Long.class)))
//    })
//    @PatchMapping("/boards/{boardId}/replys/{replyId}")
//    public ResponseEntity<Long> modify(@Parameter(description = "게시글 ID",required = true) @PathVariable Long boardId,@Parameter(description = "댓글 ID",required = true) @PathVariable Long replyId,
//        @RequestBody BoardrpRequest request,@Parameter(hidden = true) @AuthMember Member member) {
//
//        boardrpService.modify(boardId,replyId, request, member.getId());
//        return ResponseEntity.ok().body(replyId);
//
//    }

    @Operation(summary = "게시글 관련 댓글 삭제 요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH_TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "게시글 관련 댓글 삭제 요청"),
        @ApiResponse(responseCode = "404", description = "게시글 아이디 혹은 댓글 아이디가 잘못되었습니다", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "403", description = "접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/boards/{boardId}/replys/{replyId}")
    public ResponseEntity delete(
        @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId,
        @Parameter(description = "댓글 ID", required = true) @PathVariable Long replyId,
        @Parameter(hidden = true) @AuthMember Member member) {

        boardrpService.delete(boardId, replyId, member.getId());
        return ResponseEntity.ok().build();
    }
}
