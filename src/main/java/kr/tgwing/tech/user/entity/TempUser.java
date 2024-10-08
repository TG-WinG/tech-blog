package kr.tgwing.tech.user.entity;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kr.tgwing.tech.admin.dto.AdminCheckUserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;

@Entity
@Getter
@Table(name = "temp_user")
@DiscriminatorValue("F")
@NoArgsConstructor
public class TempUser extends BaseUser{

    @Builder
    public TempUser(String studentId, String password, String email, String name, Date birth, String phoneNumber) {
        super(studentId, password, email, name, birth, phoneNumber);
    }

    public AdminCheckUserDto toAdminCheckUserDto(TempUser user) {
        return AdminCheckUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .studentId(user.getStudentId())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public User toUser(TempUser user) {
        return User.builder()
                .studentId(user.getStudentId())
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



