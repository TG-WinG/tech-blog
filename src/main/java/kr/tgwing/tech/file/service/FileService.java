package kr.tgwing.tech.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadImage(String name, MultipartFile image);
}
