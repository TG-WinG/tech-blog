package kr.tgwing.tech.blog.dto;

import kr.tgwing.tech.blog.entity.ReplyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ReplyDto {

    private Long id;
    private Long writer;
    private String description;
    private LocalDateTime modDate;

    public static ReplyEntity toEntity(ReplyDto replyDto, Long postId) {
        return ReplyEntity.builder()
                .writer(replyDto.writer)
                .description(replyDto.description)
                .post(postId)
                .build();
    }
}
