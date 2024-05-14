package kr.tgwing.tech.user.entity;

import jakarta.persistence.*;
import kr.tgwing.tech.common.BaseEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "users")
public class UserEntity extends BaseEntity implements Serializable {
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

    @Column
    private String role;

    @Column
    private String profilePicture;
}