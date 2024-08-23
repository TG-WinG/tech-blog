package kr.tgwing.tech.project.repository;

import kr.tgwing.tech.project.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Long> {
}
