package kr.tgwing.tech.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailMessageDTO {
    private String receiver;
    private String subject;
    private String message;
}
