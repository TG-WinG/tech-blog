package kr.tgwing.tech.admin.dto;

import kr.tgwing.tech.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllStudentsDto {
    private Long studentId;
    private String studentNumber;
    private String email;
    private String name;
    private String phoneNumber;
    private LocalDate birth;

    public static AllStudentsDto of(User user) {
        return AllStudentsDto.builder()
                .studentId(user.getStudentId())
                .studentNumber(user.getStudentNumber())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .birth(user.getBirth())
                .build();
    }
}
