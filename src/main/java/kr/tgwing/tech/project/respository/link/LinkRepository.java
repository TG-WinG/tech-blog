package kr.tgwing.tech.project.respository.link;

import kr.tgwing.tech.project.domain.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<LinkEntity, Long> {
    @Modifying
    @Query("DELETE FROM LinkEntity p WHERE p.project.id = :projectId")
    void deleteByProjectId(@Param("projectId") Long projectId);
}
