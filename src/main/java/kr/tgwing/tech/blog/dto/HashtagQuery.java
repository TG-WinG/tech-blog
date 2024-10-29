package kr.tgwing.tech.blog.dto;

import lombok.Data;

/**
 * HashtagQuery
 */
@Data
public class HashtagQuery {

    private String keyword = "";
    private boolean me = false;

}
