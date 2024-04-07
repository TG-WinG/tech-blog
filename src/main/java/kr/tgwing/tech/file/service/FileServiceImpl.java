package kr.tgwing.tech.file.service;

import kr.tgwing.tech.utils.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {
    private final S3Uploader s3Uploader;

    public FileServiceImpl(S3Uploader s3Uploader) {
        this.s3Uploader = s3Uploader;
    }

    @Override
    @Transactional
    public String uploadImage(String name, MultipartFile image) {
        String url = "";
        if(image != null)  url = s3Uploader.upload("static/image/" + name, image);
        return url;
    }
}
