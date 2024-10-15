package kr.tgwing.tech.blog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import kr.tgwing.tech.blog.entity.Post;
import kr.tgwing.tech.user.entity.User;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    Optional<Post> findById(Long id);
    @Override
    List<Post> findAll();

    List<Post> findByWriter(User writer);

    Page<Post> findAllByOrderByIdDesc(Pageable pageable);

    Page<Post> findByTitleContains(String search, Pageable pageable);

    long countByTitleContains(String search);

    Page<Post> findByWriter(User writer, Pageable pageable);
    @Override
    long count();
}
