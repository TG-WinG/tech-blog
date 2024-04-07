package kr.tgwing.tech.respository.participant;

import kr.tgwing.tech.domain.project.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long> {
}
