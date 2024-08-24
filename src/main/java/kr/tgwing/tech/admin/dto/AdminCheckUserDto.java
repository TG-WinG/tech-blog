package kr.tgwing.tech.admin.dto;

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
}
