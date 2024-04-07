package kr.tgwing.tech.respository.link;

import kr.tgwing.tech.domain.project.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<LinkEntity, Long> {
}
