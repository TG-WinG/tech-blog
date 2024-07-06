package kr.tgwing.tech.blog.repository;

import kr.tgwing.tech.blog.entity.HashTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<HashTagEntity, Long> {
    Optional<HashTagEntity> findByName(String name);
}
