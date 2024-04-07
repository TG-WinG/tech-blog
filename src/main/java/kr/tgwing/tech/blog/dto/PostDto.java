package kr.tgwing.tech.blog.dto;


import kr.tgwing.tech.blog.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PostDto {
    private Long writer;
    private String title;
    private String content;
    private String thumbnail;

    public static PostEntity toEntity(PostDto postDto) {
        return PostEntity.builder()
                .writer(postDto.writer)
                .title(postDto.title)
                .content(postDto.content)
                .thumbnail(postDto.thumbnail)
                .build();
    }

//    public static PostEntity toEntityWithId (PostDto postDto, Long writerId) {
//        return PostEntity.builder()
//                .writer(writerId)
//                .title(postDto.title)
//                .content(postDto.content)
//                .thumbnail(postDto.thumbnail).build();
//    }
}
