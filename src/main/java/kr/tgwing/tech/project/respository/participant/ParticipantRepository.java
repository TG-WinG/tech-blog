package kr.tgwing.tech.project.respository.participant;

import kr.tgwing.tech.project.domain.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long> {
    @Modifying
    @Query("DELETE FROM ParticipantEntity p WHERE p.project.id = :projectId")
    void deleteByProjectId(@Param("projectId") Long projectId);
}
