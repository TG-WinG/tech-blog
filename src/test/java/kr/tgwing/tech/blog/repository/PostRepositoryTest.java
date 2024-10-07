package kr.tgwing.tech.blog.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import kr.tgwing.tech.blog.entity.Post;
import kr.tgwing.tech.blog.entity.PostSpecifications;

@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void findAllPostsByTitleContainsKeywordAndHashtagIsInHashtags() {
        Set<String> hashtags = new HashSet<>();
        hashtags.add("tag1");
        hashtags.add("tag2");

        Specification<Post> spec = PostSpecifications.hasTitleLike("1")
            .and(PostSpecifications.hasContentLike("1"))
            .and(PostSpecifications.hasHashtagIn(hashtags));

        List<Post> all = postRepository.findAll(spec);

        System.out.println(all.size());
        assertTrue(all.size() == 1);
    }
}