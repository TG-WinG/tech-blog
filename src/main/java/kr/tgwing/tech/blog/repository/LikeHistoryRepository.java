package kr.tgwing.tech.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.tgwing.tech.blog.entity.LikeHistory;

/**
 * LikeHistoryRepository
 */
public interface LikeHistoryRepository extends JpaRepository<LikeHistory, LikeHistory.Key> {

}
