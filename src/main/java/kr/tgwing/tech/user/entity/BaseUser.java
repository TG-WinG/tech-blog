package kr.tgwing.tech.user.entity;

import jakarta.persistence.*;
import kr.tgwing.tech.common.BaseEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;


@NoArgsConstructor
@MappedSuperclass
@Getter
public abstract class BaseUser extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    @Column(length = 10, unique = true, nullable = false)
    private String studentNumber;

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

    public BaseUser(String studentNumber, String password, String email, String name, Date birth, String phoneNumber) {
        this.studentNumber = studentNumber;
        this.password = password;
        this.email = email;
        this.name = name;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

}
