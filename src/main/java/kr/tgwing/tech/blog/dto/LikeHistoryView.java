package kr.tgwing.tech.blog.dto;

import lombok.Builder;
import lombok.Data;

import kr.tgwing.tech.blog.entity.LikeHistory;

/**
 * LikeHistoryView
 */
@Data
@Builder
public class LikeHistoryView {

    private Long postId;
    private Long userStudentId;
    private boolean canceled;

    public static LikeHistoryView of(LikeHistory history) {
        return LikeHistoryView.builder()
                .postId(history.getPost().getId())
                .userStudentId(history.getUser().getStudentId())
                .canceled(history.isCanceled())
                .build();
    }
}
