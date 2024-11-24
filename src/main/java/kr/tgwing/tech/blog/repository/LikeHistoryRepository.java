package kr.tgwing.tech.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.tgwing.tech.blog.entity.LikeHistory;
import kr.tgwing.tech.blog.entity.Post;

/**
 * LikeHistoryRepository
 */
public interface LikeHistoryRepository extends JpaRepository<LikeHistory, LikeHistory.Key> {

    void deleteAllByPost(Post post);
}
