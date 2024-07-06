package kr.tgwing.tech.blog.dto;

//import kr.tgwing.tech.files.dto.FileResponseDto;
import kr.tgwing.tech.blog.entity.HashTagEntity;
import kr.tgwing.tech.blog.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class PostDto {
    private Long id;
    private Long writer;
    private String title;
    private String content;
    private String thumbnail;
    private Set<String> hashtags;

    public static PostEntity toEntity(PostDto postRequestDto, Set<HashTagEntity> hashtags) {
        return PostEntity.builder()
                .writer(postRequestDto.writer)
                .title(postRequestDto.title)
                .content(postRequestDto.content)
                .thumbnail(postRequestDto.thumbnail)
                .hashtags(hashtags)
                .build();
    }
}
