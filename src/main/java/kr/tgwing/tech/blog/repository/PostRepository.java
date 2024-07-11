package kr.tgwing.tech.blog.repository;


import kr.tgwing.tech.blog.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Optional<PostEntity> findById(Long id);
    @Override
    List<PostEntity> findAll();


    Page<PostEntity> findAllByOrderByIdDesc(Pageable pageable);

    Page<PostEntity> findByTitleContains(String search, Pageable pageable);

    long countByTitleContains(String search);
    @Override
    long count();



}
