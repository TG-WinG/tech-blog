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
public class PostOverview {

    public static final int SUMMARY_LENGTH = 30;

    private Long id;
    private ProfileDTO writer;
    private String title;
    private String summary;
    private String thumbnail;
    private String modDate;
    private int likeCount;
    private int commentCount;
    @Singular private final Set<String> hashtags;

    public static PostOverview of(Post post) {
        String content = post.getContent();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return PostOverview.builder()
                .id(post.getId())
                .writer(ProfileDTO.of(post.getWriter()))
                .title(post.getTitle())
                .summary((content.length() < SUMMARY_LENGTH) ? content : content.substring(0, SUMMARY_LENGTH))
                .thumbnail(post.getThumbnail())
                .modDate(post.getModDate().format(formatter))
                .commentCount(post.getCommentCount())
                .likeCount(post.getLikeCount())
                .hashtags(post.getHashtags().stream().map(hashtag -> hashtag.getName()).toList())
                .build(); 
    }

}
