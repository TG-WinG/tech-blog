package kr.tgwing.tech.blog.entity;

import jakarta.persistence.*;
import kr.tgwing.tech.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(PostTagId.class)
@Table(name = "post_hashtag")
public class PostTagEntity extends BaseEntity {

    @Id
    private Long postId;

    @Id
    private Long hashtagId;


    @ManyToOne(fetch = FetchType.LAZY)
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    private HashTagEntity hashtag;

}
