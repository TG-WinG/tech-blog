package kr.tgwing.tech.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import kr.tgwing.tech.file.dto.DownloadResponse;
import kr.tgwing.tech.file.dto.ImageResponse;
import kr.tgwing.tech.file.exception.FileEmptyException;
import kr.tgwing.tech.file.exception.FileUploadFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Profile("dev")
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    final static String IMAGE_DIR = "image/";
    final static String ATTACHMENT_DIR = "attachment/";
    private static final String TIME_SEPARATOR = "_";

    @Override
    public String uploadImage(MultipartFile image) {
        UUID uuid = UUID.randomUUID();
        if (image == null) throw new FileEmptyException();
        upload(IMAGE_DIR + uuid, image);
        return uuid.toString();
    }

    @Override
    public String uploadFile(MultipartFile file) {
        if (file == null) throw new FileEmptyException();

        String filename = file.getOriginalFilename();
        String now = String.valueOf(System.currentTimeMillis());
        String uploadName = now + TIME_SEPARATOR + filename;
        upload(ATTACHMENT_DIR + uploadName, file);
        return uploadName;
    }

    @Override
    public ImageResponse getImage(UUID uuid) {
        String resourcePath = IMAGE_DIR + uuid;
        S3Object object = amazonS3Client.getObject(bucket, resourcePath);
        S3ObjectInputStream content = object.getObjectContent();
        ObjectMetadata metadata = object.getObjectMetadata();

        HttpHeaders headers = makeContentHeaders(metadata);

        return new ImageResponse(content, headers);
    }


    @Override
    public DownloadResponse downloadFile(String filename) {
        String resourcePath = ATTACHMENT_DIR + filename;
        S3Object object = amazonS3Client.getObject(bucket, resourcePath);
        S3ObjectInputStream content = object.getObjectContent();
        ObjectMetadata metadata = object.getObjectMetadata();

        HttpHeaders headers = makeContentHeaders(metadata);

        return new DownloadResponse(filename, content, headers);
    }

    private void upload(String filename, MultipartFile multipartFile) {
        validateFileExists(multipartFile);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, filename, inputStream, objectMetadata));
        } catch (IOException e) {
            throw new FileUploadFailException();
        }
    }

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new FileEmptyException();
        }
    }

    private HttpHeaders makeContentHeaders(ObjectMetadata metadata) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(metadata.getContentLength()));
        headers.add(HttpHeaders.CONTENT_TYPE, metadata.getContentType());
        return headers;
    }
}
