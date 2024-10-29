package kr.tgwing.tech.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import kr.tgwing.tech.blog.entity.Hashtag;

/**
 * HashtagRepository
 */
public interface HashtagRepository extends JpaRepository<Hashtag, Long>, JpaSpecificationExecutor<Hashtag> {

}
