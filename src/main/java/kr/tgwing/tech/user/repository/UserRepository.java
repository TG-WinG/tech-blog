package kr.tgwing.tech.user.repository;

import jakarta.transaction.Transactional;
import kr.tgwing.tech.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByStudentId(String studentId);

    @Transactional
    void deleteByStudentId(String studentId);


    @Transactional
    @Modifying
    @Query("UPDATE User U SET U.name = :name, U.phoneNumber = :phoneNumber, U.profilePicture = :profilePicture WHERE U.studentId = :id")
    void changeUser(String id, String name, String phoneNumber, String profilePicture);

}

