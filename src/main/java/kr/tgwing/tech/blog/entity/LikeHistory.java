package kr.tgwing.tech.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import kr.tgwing.tech.common.BaseEntity;
import kr.tgwing.tech.user.entity.User;

/**
 * LikeHistory
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "like_history")
public class LikeHistory extends BaseEntity {

    @EmbeddedId
    private Key key;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "canceled")
    private boolean canceled;

    @Embeddable
    public static record Key(Long userId, Long postId) {}

    public void toggle() {
        if (canceled) canceled = false;
        else canceled = true;
    }

}
