package kr.tgwing.tech.file.controller;

import kr.tgwing.tech.file.service.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Profile("dev")
@Controller
@RequiredArgsConstructor
public class FileController {

    @Autowired
    FileServiceImpl fileServiceImpl;

    @PostMapping(path = "/file/image")
    public ResponseEntity<String> uploadImage(
            @RequestPart(value = "name") String name,
            @RequestPart(value = "image") MultipartFile image
    ){
        return ResponseEntity.ok(fileServiceImpl.uploadImage(name, image));
    }
}
