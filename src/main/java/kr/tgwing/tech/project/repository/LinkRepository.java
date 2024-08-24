package kr.tgwing.tech.project.repository;

import kr.tgwing.tech.project.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Long> {
    List<Link> findAllByProjectId(Long projectId);

    @Modifying
    @Query("DELETE FROM Link l WHERE l.project IS NULL")
    void deleteAllIfProjectIdIsNull();

}
