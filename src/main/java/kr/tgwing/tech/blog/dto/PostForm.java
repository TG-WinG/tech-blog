package kr.tgwing.tech.blog.dto;

import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostForm {

    private String title;
    private String content;
    private String thumbnail;
    private Set<String> hashtags;

}
