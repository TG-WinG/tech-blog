package kr.tgwing.tech.reply.dto;

import kr.tgwing.tech.reply.entity.ReplyEntity;

public class ReplyCreationDto {

    private String description;

    public static ReplyEntity toEntity(ReplyCreationDto dto, Long postId) {
        return ReplyEntity.builder()
                .description(dto.description)
                .post(postId)
                .build();
    }
}
