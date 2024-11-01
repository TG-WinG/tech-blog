package kr.tgwing.tech.user.repository;

import kr.tgwing.tech.user.entity.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.awt.print.Pageable;

public interface TempUserRepository extends JpaRepository<TempUser, Long>, JpaSpecificationExecutor<TempUser> {

}
