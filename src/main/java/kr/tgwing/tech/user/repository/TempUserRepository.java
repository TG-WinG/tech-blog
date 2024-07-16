package kr.tgwing.tech.user.repository;

import kr.tgwing.tech.user.entity.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempUserRepository extends JpaRepository<TempUser, Long> {

}
