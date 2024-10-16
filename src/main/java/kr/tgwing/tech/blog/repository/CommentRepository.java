package kr.tgwing.tech.blog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.tgwing.tech.blog.entity.Comment;
import kr.tgwing.tech.blog.entity.Post;

/**
 * CommentRepository
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);
    Page<Comment> findAllByPost(Post post, Pageable pageable);

}
