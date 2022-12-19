package com.example.SSU_Rental.image;

import com.example.SSU_Rental.config.LogExecutionTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * 토큰 검증 과정이 적용X
 */
@Tag(name = "사진 업로드",description = "사진 업로드 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class AmazonS3Controller {
    private final AwsS3Service awsS3Service;

    /**
     * Amazon S3에 이미지 업로드
     * @return 성공 시 200 Success와 함께 업로드 된 파일의 파일명 리스트 반환
     */
    @Operation(summary = "S3서버에 사진 업로드 요청",parameters={@Parameter(in = ParameterIn.HEADER,name = "X-AUTH_TOKEN",required = true,description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "아이템 등록 성공", content = @Content(schema = @Schema(implementation = List.class)))
    })
    @PostMapping(value = "/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @LogExecutionTime
    public ResponseEntity<List<String>> uploadImage(@RequestPart List<MultipartFile> multipartFile) {
         return   ResponseEntity.ok().body(awsS3Service.uploadImage(multipartFile));
    }

    /**
     * Amazon S3에 이미지 업로드 된 파일을 삭제
     * @return 성공 시 200 Success
     */

    @Operation(summary = "S3내 저장된 사진 삭제 요청",parameters={@Parameter(in = ParameterIn.HEADER,name = "X-AUTH_TOKEN",required = true,description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "사진 삭제 성공")
    })
    @DeleteMapping("/image")
    @LogExecutionTime
    public ResponseEntity deleteImage(@RequestParam String fileName) {
        awsS3Service.deleteImage(fileName);
        return ResponseEntity.ok().build();
    }
}
