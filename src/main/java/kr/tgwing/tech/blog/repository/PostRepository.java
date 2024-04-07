package kr.tgwing.tech.blog.repository;


import kr.tgwing.tech.blog.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Optional<PostEntity> findById(Long id);
    @Override
    List<PostEntity> findAll();

//    List<PostEntity> findByTitleContains(String search);
//    List<PostEntity> findByContentContains(String search);
    Page<PostEntity> findAllByOrderByIdDesc(Pageable pageable);

    Page<PostEntity> findByTitleContains(String search, Pageable pageable);
//    @Query(value = "select * from post p where  p.type = 0", nativeQuery = true)
//    List<PostEntity> findAllNotice();

//    @Query(value = "select count(*) from post p where p.type = 0", nativeQuery = true)
//    int getCount();
    long countByTitleContains(String search);
    @Override
    long count();
}
