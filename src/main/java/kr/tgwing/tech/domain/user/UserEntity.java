package kr.tgwing.tech.domain.user;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Builder
@DynamicInsert
@NoArgsConstructor @AllArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 점직적 증가
    private Long id; // 기본 키.

    @Column(nullable = false, unique = true)
    private String studentId; // 아이디

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false, unique = true, length = 25)
    private String email;

    @Column(nullable = false)
    private String name; // 이름


    @Column(nullable = false, length = 9)
    private String semester; // 학년, 학기(3글자로 설정)

    @Column(nullable = false, unique = true, length = 13)
    private String phoneNumber; // 전화번호(13글자로 설정)


}
