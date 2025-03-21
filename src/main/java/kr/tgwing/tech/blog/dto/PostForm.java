package kr.tgwing.tech.blog.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostForm {

    private String title;
    private String content;
    private String thumbnail;
    private Set<String> hashtags;

}
