package kr.tgwing.tech.project.respository.participant;

import kr.tgwing.tech.project.domain.OutsiderParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OutsiderParticipantRepository extends JpaRepository<OutsiderParticipantEntity, Long> {
    @Modifying
    @Query("DELETE FROM OutsiderParticipantEntity p WHERE p.project.id = :projectId")
    void deleteByProjectId(@Param("projectId") Long projectId);


}
