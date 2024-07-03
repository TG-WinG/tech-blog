package kr.tgwing.tech.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Date;

@Entity
@Getter
@DiscriminatorValue("M")
@NoArgsConstructor
@Table(name = "users")
public class UserEntity extends User {

    @Column
    private String role;

    @Column
    private String profilePicture;

    @Builder
    public UserEntity (String studentId, String password, String email, String name, Date birth, String phoneNumber, String role, String profilePicture) {
        super(studentId, password, email, name, birth, phoneNumber);
        this.role = role;
        this.profilePicture = profilePicture;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
