package kr.tgwing.tech.user.dto.checkdto;

import kr.tgwing.tech.user.dto.EmailMessageDTO;
import kr.tgwing.tech.user.dto.registerdto.EmailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class CheckUserDTO {
    private String studentNumber;
    private String name;
    private String email;

    public EmailMessageDTO toCheckPassword(CheckUserDTO checkUserDTO) {
        return EmailMessageDTO.builder()
                .receiver(checkUserDTO.getEmail())
                .subject("[TGWING} 인증코드")
                .build();
    }
}
