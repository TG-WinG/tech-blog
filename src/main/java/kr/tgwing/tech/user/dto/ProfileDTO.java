package kr.tgwing.tech.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    private String studentId;
    private String email;
    private String name; // 이름
    private Date birth;
    private String phoneNumber;
    private String profilePicture;

}
