package kr.tgwing.tech.project.repository;

import kr.tgwing.tech.project.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findAllByProjectId(Long projectId);
}
