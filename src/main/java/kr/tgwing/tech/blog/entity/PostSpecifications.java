package kr.tgwing.tech.blog.entity;

import java.util.Set;

import jakarta.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

/**
 * PostSpecification
 */
public class PostSpecifications {

    public static Specification<Post> hasTitleLike(String keyword) {
        return (root, query, cb) -> 
            cb.like(root.<String>get("title"), "%" + keyword + "%");
    }

    public static Specification<Post> hasContentLike(String keyword) {
        return (root, query, cb) ->
            cb.like(root.<String>get("content"), "%" + keyword + "%");
    }

    public static Specification<Post> hasHashtag(String hashtag) {
        return (root, query, cb) -> {
            Join<Hashtag, Post> hashtagPost = root.join("hashtags");
            return cb.equal(hashtagPost.get("name"), hashtag);
        };
    }

    public static Specification<Post> hasHashtagIn(Set<String> hashtags) {
        return (root, query, cb) -> {
            Join<Hashtag, Post> hashtagPost = root.join("hashtags");
            return hashtagPost.get("name").in(hashtags);
        };
    }

}
