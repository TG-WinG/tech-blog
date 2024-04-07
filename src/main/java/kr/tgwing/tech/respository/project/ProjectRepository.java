package kr.tgwing.tech.respository.project;

import kr.tgwing.tech.domain.project.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
}
