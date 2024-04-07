package kr.tgwing.tech.project.respository.link;

import kr.tgwing.tech.project.domain.project.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<LinkEntity, Long> {
}
