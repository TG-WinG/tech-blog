package kr.tgwing.tech.project.respository.project;

import kr.tgwing.tech.project.domain.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Optional<ProjectEntity> findById(Long id);

    @Override
    List<ProjectEntity> findAll();

    Page<ProjectEntity> findAllByOrderByIdDesc(Pageable pageable);

    Page<ProjectEntity> findByTitleContains(String search, Pageable pageable);

    long countByTitleContains(String search);

    @Override
    long count();
}
