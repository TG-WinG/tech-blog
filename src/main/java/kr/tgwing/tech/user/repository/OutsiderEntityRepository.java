package kr.tgwing.tech.user.repository;

import jakarta.transaction.Transactional;
import kr.tgwing.tech.user.entity.OutsiderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OutsiderEntityRepository extends JpaRepository<OutsiderEntity, Long> {
    Optional<OutsiderEntity> findByStudentId(String studentId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM OutsiderEntity o WHERE o.id IN (SELECT op.outsiderEntity.id FROM OutsiderParticipantEntity op WHERE op.project.id = :projectId)")
    void deleteByProjectId(Long projectId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM OutsiderEntity o WHERE o.id NOT IN (SELECT DISTINCT op.outsiderEntity.id FROM OutsiderParticipantEntity op)")
    void deleteOutsidersNotInAnyProject();
}
