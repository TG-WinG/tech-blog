package kr.tgwing.tech.blog.entity;

import jakarta.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import kr.tgwing.tech.user.entity.User;

/**
 * HashtagSpecifications
 */
public class HashtagSpecifications {

    public static Specification<Hashtag> hasNameLike(String keyword) {
        return (root, query, cb) -> 
            cb.like(root.<String>get("name"), "%" + keyword + "%");
    }

    public static Specification<Hashtag> hasStudentWithStudentNumber(String studentNumber) {
        return (root, query, cb) -> {
            Join<Hashtag, Post> hashtagPost = root.join("post");
            Join<Post, User> postHashtag = hashtagPost.join("writer");
            return cb.equal(postHashtag.get("studentNumber"), studentNumber);
        };
    }
}

