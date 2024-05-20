package kr.tgwing.tech.blog.dto;

//import kr.tgwing.tech.files.dto.FileResponseDto;
import kr.tgwing.tech.blog.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PostDto {
    private Long id;
    private Long writer;
    private String title;
    private String content;
    private String thumbnail;

    public static PostEntity toEntity(PostDto postRequestDto) {
        return PostEntity.builder()
                .writer(postRequestDto.writer)
                .title(postRequestDto.title)
                .content(postRequestDto.content)
                .thumbnail(postRequestDto.thumbnail)
                .build();
    }
}
