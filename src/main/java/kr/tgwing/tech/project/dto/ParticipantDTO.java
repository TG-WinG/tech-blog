package kr.tgwing.tech.project.dto;

import jakarta.validation.constraints.NotNull;
import kr.tgwing.tech.project.domain.Enum.DevRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantDTO {

    @NotNull
    private DevRole devRole;
    @NotNull
    private String studentId;
    @NotNull
    private String major;
    @NotNull
    private String name;

    @Builder
    public ParticipantDTO(DevRole devRole, String studentId, String major, String name) {
        this.devRole = devRole;
        this.studentId = studentId;
        this.major = major;
        this.name = name;
    }


}
