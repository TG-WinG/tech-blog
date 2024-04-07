package kr.tgwing.tech.dto;

import kr.tgwing.tech.domain.project.LinkEntity;
import kr.tgwing.tech.domain.project.ParticipantEntity;
import kr.tgwing.tech.domain.project.ProjectEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectUpdateDTO {
    private String title;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private String thumbnail;
    private String devStatus;
    private String devType;
    private List<ParticipantEntity> participants = new ArrayList<>();
    private List<LinkEntity> links = new ArrayList<>();

    @Builder
    public ProjectUpdateDTO(String title, String description, LocalDateTime start, LocalDateTime end, String thumbnail, String devStatus, String devType, List<ParticipantEntity> participants, List<LinkEntity> links) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.thumbnail = thumbnail;
        this.devStatus = devStatus;
        this.devType = devType;
        this.participants = participants;
        this.links = links;
    }

    public static ProjectEntity toEntity(ProjectUpdateDTO projectUpdateDTO){
        return ProjectEntity.builder()
                .title(projectUpdateDTO.getTitle())
                .description(projectUpdateDTO.getDescription())
                .start(projectUpdateDTO.getStart())
                .end(projectUpdateDTO.getEnd())
                .devStatus(projectUpdateDTO.getDevStatus())
                .devType(projectUpdateDTO.getDevType())
                .participants(projectUpdateDTO.getParticipants())
                .links(projectUpdateDTO.getLinks())
                .build();
    }
}
