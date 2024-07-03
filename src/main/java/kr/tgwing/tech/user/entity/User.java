package kr.tgwing.tech.user.entity;

import jakarta.persistence.*;
import kr.tgwing.tech.common.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@DiscriminatorColumn(name = "USER_TYPE")
@Table(name = "tempUsers")
public class User extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, unique = true, nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 25)
    private String email;

    @Column(nullable = false)
    private String name; // 이름

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;

    @Column(nullable = false, length = 13)
    private String phoneNumber;

    public User (String studentId, String password, String email, String name, Date birth, String phoneNumber) {
        this.studentId = studentId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

}
