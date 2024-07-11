package kr.tgwing.tech.blog.dto;

import kr.tgwing.tech.blog.entity.HashTagEntity;
import kr.tgwing.tech.blog.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class PostCreationDto {
    private String title;
    private String content;
    private String thumbnail;
    private Set<String> hashtags;

    public static PostEntity toEntity(PostCreationDto dto) {
        return PostEntity.builder()
                .title(dto.title)
                .content(dto.content)
                .thumbnail(dto.thumbnail)
                .build();
    }

}
