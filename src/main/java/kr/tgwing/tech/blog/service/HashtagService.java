package kr.tgwing.tech.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.tgwing.tech.blog.dto.HashtagQuery;
import kr.tgwing.tech.blog.dto.HashtagView;

/**
 * HashtagService
 */
public interface HashtagService {

    Page<HashtagView> getHashtags(HashtagQuery query, String userStudentNumber, Pageable pageable);

}
