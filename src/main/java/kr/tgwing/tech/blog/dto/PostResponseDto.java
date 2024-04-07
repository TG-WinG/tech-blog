package kr.tgwing.tech.blog.dto;

//import kr.tgwing.tech.files.dto.FileResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PostResponseDto {
    private Long writer;
    private String title;
    private String content;
    private String thumbnail;
//    private List<FileResponseDto> images;
//    private List<FileResponseDto> files;

}
