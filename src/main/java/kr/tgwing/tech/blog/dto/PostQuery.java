package kr.tgwing.tech.blog.dto;

import java.util.Set;

import lombok.Data;

@Data
public class PostQuery {

    private String keyword = "";
    private Set<String> hashtag;
    private boolean me = false;

}
