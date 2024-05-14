package kr.tgwing.tech.file.service;

import kr.tgwing.tech.file.dto.DownloadResponse;
import kr.tgwing.tech.file.dto.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileService {
    String uploadImage(MultipartFile image);
    String uploadFile(MultipartFile file);
    ImageResponse getImage(UUID uuid);
    DownloadResponse downloadFile(String filename);

}
