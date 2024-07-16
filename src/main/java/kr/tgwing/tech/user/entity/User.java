package kr.tgwing.tech.user.entity;

import jakarta.persistence.*;
import kr.tgwing.tech.user.dto.profiledto.ProfileDTO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Entity
@Getter
@DiscriminatorValue("M")
@NoArgsConstructor
@Table(name = "user")
public class User extends BaseUser {

    @Column
    private String role;

    @Column
    private String profilePicture;

    @Builder
    public User(String studentId, String password, String email, String name, Date birth, String phoneNumber, String role, String profilePicture) {
        super(studentId, password, email, name, birth, phoneNumber);
        this.role = role;
        this.profilePicture = profilePicture;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ProfileDTO toProfileDTO(User user) {
        return ProfileDTO.builder()
                .studentId(user.getStudentId())
                .email(user.getEmail())
                .name(user.getName())
                .birth(user.getBirth())
                .phoneNumber(user.getPhoneNumber())
                .profilePicture(user.getProfilePicture())
                .build();
    }
}
