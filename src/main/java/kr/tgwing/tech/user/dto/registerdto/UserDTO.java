package kr.tgwing.tech.user.dto.registerdto;

import kr.tgwing.tech.user.entity.TempUser;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class UserDTO {

    private String studentNumber;
    private String password;
    private String email;
    private String name; // 이름
    private LocalDate birth;
    private String phoneNumber;

    public static TempUser toTempUser(UserDTO userDTO) {
        return TempUser.builder()
                .studentNumber(userDTO.getStudentNumber())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .birth(userDTO.getBirth())
                .phoneNumber(userDTO.getPhoneNumber())
                .build();


    }

}
