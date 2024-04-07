package kr.tgwing.tech.project.respository.participant;

import kr.tgwing.tech.project.domain.project.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long> {
}
