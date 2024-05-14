package kr.tgwing.tech.file.controller;

import kr.tgwing.tech.file.dto.DownloadResponse;
import kr.tgwing.tech.file.dto.ImageResponse;
import kr.tgwing.tech.file.service.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

@Profile("dev")
@Controller
@RequiredArgsConstructor
@Slf4j
public class FileController {

    @Autowired
    FileServiceImpl fileServiceImpl;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${server.domain}")
    private String domain;

    @PostMapping(path = "/file/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(
            @RequestPart(value = "image") MultipartFile image
    ){
        String uuid = fileServiceImpl.uploadImage(image);
        String url = "http://" + domain + contextPath + "/file/image?uuid=" + uuid;
        return ResponseEntity.ok(url);
    }

    @PostMapping(path = "/file/attachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAttachment(
            @RequestPart(value = "file") MultipartFile file
    ){
        String filename = fileServiceImpl.uploadFile(file);
        String url = "http://" + domain + contextPath + "/file/attachment?filename=" + filename;
        return ResponseEntity.ok(url);
    }

    @GetMapping("/file/image")
    public ResponseEntity<InputStreamResource> getImage (
            @RequestParam("uuid") UUID uuid
    ) {
        ImageResponse response = fileServiceImpl.getImage(uuid);
        return new ResponseEntity<>(new InputStreamResource(response.getInputStream()), response.getHeaders(), HttpStatus.OK);
    }

    @GetMapping("/file/attachment")
    public ResponseEntity<InputStreamResource> downloadAttachment (
            @RequestParam("filename") String filename
    ){
        DownloadResponse response = fileServiceImpl.downloadFile(filename);
        return new ResponseEntity<>(new InputStreamResource(response.getInputStream()), response.getHeaders(), HttpStatus.OK);
    }
}
