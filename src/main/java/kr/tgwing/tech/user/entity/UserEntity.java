package kr.tgwing.tech.user.entity;

import jakarta.persistence.*;
import kr.tgwing.tech.common.BaseEntity;
import kr.tgwing.tech.project.domain.ParticipantEntity;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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

    @Column
    private Date birth;

    @Column(nullable = false, length = 11)
    private String phoneNumber;

    @Column
    private String role;

    private String profilePicture;

    @OneToMany(mappedBy = "userEntity",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ParticipantEntity> participants = new ArrayList<>();
}
