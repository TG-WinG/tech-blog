package kr.tgwing.tech.user.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import kr.tgwing.tech.user.dto.profiledto.ProfileDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@DiscriminatorValue("M")
@NoArgsConstructor
@Table(name = "student")
public class User extends BaseUser {

    @Column
    private String role;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Builder
    public User(String studentNumber, String password, String email, String name, LocalDate birth, String phoneNumber, String role, String profilePicture) {
        super(studentNumber, password, email, name, birth, phoneNumber);
        this.role = role;
        this.profilePicture = profilePicture;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            var other = (User)obj;
            return this.getStudentNumber().equals(other.getStudentNumber());
        }
        return false;
    }


    public void setRole(String role) {
        this.role = role;
    }

    public ProfileDTO toProfileDTO(User user) {
        return ProfileDTO.builder()
                .studentNumber(user.getStudentNumber())
                .email(user.getEmail())
                .name(user.getName())
                .birth(user.getBirth())
                .phoneNumber(user.getPhoneNumber())
                .profilePicture(user.getProfilePicture())
                .build();
    }

    public void hashPassword(PasswordEncoder passwordEncoder) {
        setPassword(passwordEncoder.encode(getPassword()));
    }

}
