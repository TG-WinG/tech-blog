package kr.tgwing.tech.blog.dto;

import java.time.format.DateTimeFormatter;
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
    private String modDate;
    private int likeCount;
    private int commentCount;
    @Singular private Set<String> hashtags;
    private boolean iLikeIt;

    public static PostDetail of(Post post, boolean iLikeIt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var builder = PostDetail.builder()
                .id(post.getId())
                .title(post.getTitle())
                .writer(ProfileDTO.of(post.getWriter()))
                .thumbnail(post.getThumbnail())
                .modDate(post.getModDate().format(formatter))
                .content(post.getContent())
                .commentCount(post.getCommentCount())
                .likeCount(post.getLikeCount())
                .iLikeIt(iLikeIt);

        post.getHashtags().forEach( hashtag -> builder.hashtag(hashtag.getName()) );

        return builder.build();
    }
}
