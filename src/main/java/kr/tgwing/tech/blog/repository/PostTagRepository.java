package kr.tgwing.tech.blog.repository;

import kr.tgwing.tech.blog.entity.PostTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTagEntity, Long> {

    List<PostTagEntity> findAllByPostId(Long post_id);
    void deleteAllByPostId(Long post_id);
}
