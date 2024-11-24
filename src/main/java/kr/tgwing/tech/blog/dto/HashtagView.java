package kr.tgwing.tech.blog.dto;

import lombok.Builder;
import lombok.Data;

import kr.tgwing.tech.blog.entity.Hashtag;

/**
 * HashtagView
 */
@Data
@Builder
public class HashtagView {

    private Long id;
    private String name;

    public static HashtagView of(Hashtag hashtag) {
        return HashtagView.builder()
                .id(hashtag.getId())
                .name(hashtag.getName())
                .build();
    }

}
