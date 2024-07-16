package kr.tgwing.tech.user.dto.registerdto;

import kr.tgwing.tech.user.entity.TempUser;
import kr.tgwing.tech.user.entity.User;
import lombok.*;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class UserDTO {

    private String studentId;
    private String password;
    private String email;
    private String name; // 이름
    private Date birth;
    private String phoneNumber;

    public static TempUser toTempUser(UserDTO userDTO) {
        return TempUser.builder()
                .studentId(userDTO.getStudentId())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .birth(userDTO.getBirth())
                .phoneNumber(userDTO.getPhoneNumber())
                .build();


    }



}
