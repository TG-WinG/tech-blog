package kr.tgwing.tech.blog.dto;


import kr.tgwing.tech.blog.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PostRequestDto {
    private Long writer;
    private String title;
    private String content;

    public static PostEntity toEntity(PostRequestDto postRequestDto, String thumbnailUri) {
        return PostEntity.builder()
                .writer(postRequestDto.writer)
                .title(postRequestDto.title)
                .content(postRequestDto.content)
                .thumbnail(thumbnailUri)
                .build();
    }

}
