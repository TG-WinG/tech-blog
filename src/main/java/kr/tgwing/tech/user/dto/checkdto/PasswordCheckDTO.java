package kr.tgwing.tech.user.dto.checkdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordCheckDTO {
    private String newPassword;
    private String checkPassword;
}
