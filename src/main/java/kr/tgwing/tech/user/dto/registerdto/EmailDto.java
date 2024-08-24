package kr.tgwing.tech.user.dto.registerdto;

import kr.tgwing.tech.user.dto.EmailMessageDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {
    private String email;

    public EmailMessageDTO toRegister1(EmailDto emailDto) {
        return EmailMessageDTO.builder()
                .receiver(emailDto.getEmail())
                .subject("[TGWING} 인증코드")
                .build();
    }
}
