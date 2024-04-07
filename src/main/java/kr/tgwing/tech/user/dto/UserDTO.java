package kr.tgwing.tech.user.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.tgwing.tech.user.entity.UserEntity;
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
    private String profilePicture;

    public static UserEntity toUserEntity(UserDTO userDTO) {

        return UserEntity.builder()
                .studentId(userDTO.getStudentId())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .birth(userDTO.getBirth())
                .phoneNumber(userDTO.getPhoneNumber())
                .profilePicture(userDTO.getProfilePicture())
                .build();
    }
}
