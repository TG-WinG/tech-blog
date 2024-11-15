package kr.tgwing.tech.user.repository;

import kr.tgwing.tech.user.entity.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.awt.print.Pageable;
import java.util.Optional;

public interface TempUserRepository extends JpaRepository<TempUser, Long>, JpaSpecificationExecutor<TempUser> {
    Optional<TempUser> findByStudentNumber(String studentNumber);
}
