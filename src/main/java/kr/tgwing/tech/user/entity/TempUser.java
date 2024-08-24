package kr.tgwing.tech.user.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kr.tgwing.tech.admin.dto.AdminCheckUserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "assignment_request")
@DiscriminatorValue("F")
@NoArgsConstructor
public class TempUser extends BaseUser{

    @Builder
    public TempUser(String studentNumber, String password, String email, String name, LocalDate birth, String phoneNumber) {
        super(studentNumber, password, email, name, birth, phoneNumber);
    }

    public AdminCheckUserDto toAdminCheckUserDto(TempUser user) {
        return AdminCheckUserDto.builder()
                .studentId(user.getStudentId())
                .name(user.getName())
                .email(user.getEmail())
                .studentNumber(user.getStudentNumber())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public User toUser(TempUser user) {
        return User.builder()
                .studentNumber(user.getStudentNumber())
                .password(user.getPassword())
                .email(user.getEmail())
                .name(user.getName())
                .birth(user.getBirth())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public void hashPassword(PasswordEncoder passwordEncoder) {
        setPassword(passwordEncoder.encode(getPassword()));
    }
}



