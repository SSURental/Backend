package com.example.SSU_Rental.image;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class UploadController {

    @Value("C:\\upload")
    private String uploadPath;

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<ImageDTO>> uploadFile(MultipartFile[] uploadFiles){

        List<ImageDTO> resultDTOList = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles) {
            if(uploadFile.getContentType().startsWith("image")==false){
                throw new RuntimeException("이미지 파일만 업로드 할 수 있습니다.");
            }

            String originalFilename = uploadFile.getOriginalFilename();
            String fileName = originalFilename.substring(originalFilename.lastIndexOf("\\") + 1);

            log.info("fileName={}",fileName);
            String folderPath = makeFolder();

            String uuid = UUID.randomUUID().toString();

            String saveName =
                uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            Path savePath = Paths.get(saveName);

            try {
                uploadFile.transferTo(savePath);
                String thumbnailSaveName =
                    uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_"
                        + fileName;

                File thumbnailFile = new File(thumbnailSaveName);
                Thumbnailator.createThumbnail(savePath.toFile(),thumbnailFile,100,100);
                resultDTOList.add(new ImageDTO(fileName,uuid,folderPath));
            }catch (IOException e){
                e.printStackTrace();
            }


        }
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }


    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName,String size){
        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("fileName={}",srcFileName);

            File file = new File(uploadPath + File.separator + srcFileName);
            log.info("file={}",file);

            if(size!=null&&size.equals("1")){
                file = new File(file.getParent(),file.getName().substring(2));
            }

            HttpHeaders header = new HttpHeaders();

            header.add("Content-Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),header,HttpStatus.OK);

        }catch (Exception e){

            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName){
        String srcFileName = null;
        try {
            srcFileName = URLDecoder.decode(fileName,"UTF-8");
            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result = file.delete();

            File thumbnail = new File(file.getParent(), "s_" + file.getName());
            result = thumbnail.delete();

            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    private String makeFolder(){
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/", File.separator);

        File uploadPathFolder = new File(uploadPath, folderPath);

        //폴더가 없으면 예외 발생 -> 폴더 생성
        if(uploadPathFolder.exists()==false){
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }
}
