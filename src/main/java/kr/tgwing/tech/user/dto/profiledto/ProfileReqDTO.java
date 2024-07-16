package kr.tgwing.tech.user.dto.profiledto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileReqDTO {
    private String name; // 이름
    private String phoneNumber;
    private String profilePicture;
}
