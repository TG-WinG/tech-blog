package kr.tgwing.tech.project.repository;

import kr.tgwing.tech.project.domain.Image;
import kr.tgwing.tech.project.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByProjectId(Long projectId);
}
