package kr.tgwing.tech.admin.dto;

import kr.tgwing.tech.user.entity.TempUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCheckUserDto {
    private Long studentId;
    private String studentNumber;
    private String email;
    private String name;
    private String phoneNumber;

    public static AdminCheckUserDto of(TempUser tempUser) {
        return AdminCheckUserDto.builder()
                .studentId(tempUser.getStudentId())
                .studentNumber(tempUser.getStudentNumber())
                .email(tempUser.getEmail())
                .name(tempUser.getName())
                .phoneNumber(tempUser.getPhoneNumber())
                .build();
    }
}
