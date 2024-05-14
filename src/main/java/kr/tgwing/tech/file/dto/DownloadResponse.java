package kr.tgwing.tech.file.dto;

import lombok.Getter;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Getter
public class DownloadResponse {
    InputStream inputStream;
    HttpHeaders headers;

    public DownloadResponse(String filename, InputStream inputStream, HttpHeaders headers) {
        this.inputStream = inputStream;
        if (headers == null) this.headers = new HttpHeaders();
        else this.headers = new HttpHeaders(headers);

        this.headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        this.headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build());
    }
}
