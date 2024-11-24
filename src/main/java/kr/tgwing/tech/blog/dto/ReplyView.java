package kr.tgwing.tech.blog.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

import kr.tgwing.tech.blog.entity.Reply;
import kr.tgwing.tech.user.dto.profiledto.ProfileDTO;

@Data
@Builder
public class ReplyView {

    private Long id;
    private ProfileDTO writer;
    private String content;
    private LocalDateTime modDate;

    public static ReplyView of(Reply reply) {
        return ReplyView.builder()
                .id(reply.getId())
                .writer(ProfileDTO.of(reply.getWriter()))
                .content(reply.getContent())
                .modDate(reply.getModDate())
                .build();
    }
}
