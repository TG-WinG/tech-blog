package kr.tgwing.tech.file.controller;

import kr.tgwing.tech.file.service.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Profile("dev")
@RequiredArgsConstructor
public class FileController {

    @Autowired
    FileServiceImpl fileServiceImpl;
    @PostMapping(path = "/file/image", consumes = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public ResponseEntity<String> uploadImage(
            @RequestPart(value = "name") String name,
            @RequestPart(value = "image") MultipartFile image
    ){
        return ResponseEntity.ok(fileServiceImpl.uploadImage(name, image));
    }
}
