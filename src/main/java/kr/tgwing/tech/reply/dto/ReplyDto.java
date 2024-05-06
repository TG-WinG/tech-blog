package kr.tgwing.tech.reply.dto;

import kr.tgwing.tech.common.BaseEntity;
import kr.tgwing.tech.reply.entity.ReplyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ReplyDto extends BaseEntity {

    private Long id;
    private Long writer;
    private String description;

    public static ReplyEntity toEntity(ReplyDto replyDto, Long postId) {
        return ReplyEntity.builder()
                .writer(replyDto.writer)
                .description(replyDto.description)
                .post(postId)
                .build();
    }
}
