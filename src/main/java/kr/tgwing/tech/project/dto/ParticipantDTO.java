package kr.tgwing.tech.project.dto;

import kr.tgwing.tech.project.domain.Enum.DevRole;
import kr.tgwing.tech.project.domain.project.ParticipantEntity;
import kr.tgwing.tech.project.domain.project.ProjectEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantDTO {
    private DevRole devRole;
    private String username;
    private String major;
    private ProjectEntity project;

    @Builder
    public ParticipantDTO(DevRole devRole, String username, String studentId, String major, ProjectEntity project) {
        this.devRole = devRole;
        this.username = username;
        this.major = major;
        this.project = project;
    }

    public static ParticipantEntity toParticipantEntity(ParticipantDTO participantDTO){
        return ParticipantEntity.builder()
                .devRole(participantDTO.getDevRole())
                .username(participantDTO.getUsername())
                .major(participantDTO.getMajor())
                .project(participantDTO.getProject())
                .build();
    }

}
