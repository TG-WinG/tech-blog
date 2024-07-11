package kr.tgwing.tech.blog.repository;

import kr.tgwing.tech.blog.entity.ReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
    @Query(value = "select * from reply r where r.post = :postId", nativeQuery = true)
    List<ReplyEntity> findAllByPost(Long postId);

    void deleteById(Long id);

    Page<ReplyEntity> findAllByPostOrderByModDateDesc(Pageable pageable, Long postId);
}
