package kr.tgwing.tech.blog.dto;

import java.util.Set;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import kr.tgwing.tech.blog.entity.Post;
import kr.tgwing.tech.user.dto.profiledto.ProfileDTO;

@Data
@Builder
public class PostDetail {

    private Long id;
    private ProfileDTO writer;
    private String title;
    private String content;
    private String thumbnail;
    private int likeCount;
    private int commnetCount;
    @Singular private Set<String> hashtags;

    public static PostDetail of(Post post) {
        var builder = PostDetail.builder()
                .id(post.getId())
                .title(post.getTitle())
                .writer(ProfileDTO.of(post.getWriter()))
                .content(post.getContent())
                .commnetCount(post.getComments().size());

        post.getHashtags().forEach( hashtag -> builder.hashtag(hashtag.getName()) );

        return builder.build();
    }
}
