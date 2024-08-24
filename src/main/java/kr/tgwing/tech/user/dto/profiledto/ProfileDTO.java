package kr.tgwing.tech.user.dto.profiledto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    private String studentNumber;
    private String email;
    private String name; // 이름
    private LocalDate birth;
    private String phoneNumber;
    private String profilePicture;

}
