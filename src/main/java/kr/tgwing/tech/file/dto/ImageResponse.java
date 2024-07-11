package kr.tgwing.tech.file.dto;

import java.io.InputStream;

import lombok.Getter;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;

@Getter
public class ImageResponse {
    InputStream inputStream;
    HttpHeaders headers;

    public ImageResponse(InputStream inputStream, HttpHeaders headers) {
        this.inputStream = inputStream;
        if (headers == null)
            this.headers = new HttpHeaders();
        else
            this.headers = new HttpHeaders(headers);

        this.headers.setContentDisposition(ContentDisposition.inline().build());
    }
}
