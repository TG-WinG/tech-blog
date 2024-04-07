package kr.tgwing.tech.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResultDTO {
    private String code;
    private String message;
}