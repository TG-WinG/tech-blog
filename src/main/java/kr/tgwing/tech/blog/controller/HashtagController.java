package kr.tgwing.tech.blog.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import kr.tgwing.tech.blog.dto.HashtagQuery;
import kr.tgwing.tech.blog.dto.HashtagView;
import kr.tgwing.tech.blog.service.HashtagService;

/**
 * HashtagController
 */
@RestController
@RequestMapping("/hashtag")
@AllArgsConstructor
public class HashtagController {

    private final HashtagService hashtagService;

    @GetMapping("")
    public Page<HashtagView> getHashtags(
        @ModelAttribute HashtagQuery query,
        @PageableDefault Pageable pageable,
        Principal principal
    ) {
        String studentNumber = null;
        if (principal != null) {
            studentNumber = principal.getName();
        }
        return hashtagService.getHashtags(query, studentNumber, pageable);
    }

}
