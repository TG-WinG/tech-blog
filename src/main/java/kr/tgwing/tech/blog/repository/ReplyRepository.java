package kr.tgwing.tech.blog.repository;

import java.util.List;

import kr.tgwing.tech.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.tgwing.tech.blog.entity.Comment;
import kr.tgwing.tech.blog.entity.Reply;

/**
 * ReplyRepository
 */
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Comment> findAllByComment(Comment post);
    Page<Reply> findAllByComment(Comment comment, Pageable pageable);

    List<Comment> findAllByWriter(User user);

}
