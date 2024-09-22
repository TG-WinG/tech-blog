package kr.tgwing.tech.blog.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

import kr.tgwing.tech.blog.entity.Comment;
import kr.tgwing.tech.user.dto.profiledto.ProfileDTO;

@Data
@Builder
public class CommentView {

    private Long id;
    private String content;
    private ProfileDTO writer;
    private LocalDateTime modDate;

    public static CommentView of(Comment comment) {
        return CommentView.builder()
                .content(comment.getContent())
                .writer(ProfileDTO.of(comment.getWriter()))
                .modDate(comment.getModDate())
                .build();
    }
}
