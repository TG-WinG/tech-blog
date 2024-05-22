package kr.tgwing.tech.project.dto;

import jakarta.validation.constraints.NotNull;
import kr.tgwing.tech.project.domain.LinkEntity;
import kr.tgwing.tech.project.domain.ParticipantEntity;
import kr.tgwing.tech.project.domain.ProjectEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectDetailDTO {
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    @NotNull
    private String thumbnail;
    @NotNull
    private String devStatus;
    @NotNull
    private String devType;

    @NotNull
    private List<ParticipantEntity> participants = new ArrayList<>();
    @NotNull
    private List<LinkEntity> links = new ArrayList<>();

    @Builder
    public ProjectDetailDTO(String title, String description, LocalDateTime start, LocalDateTime end, String thumbnail, String devStatus, String devType, List<ParticipantEntity> participants, List<LinkEntity> links) {
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

    public static ProjectEntity toEntity(ProjectDetailDTO projectDetailDTO){
        return ProjectEntity.builder()
                .title(projectDetailDTO.getTitle())
                .description(projectDetailDTO.getDescription())
                .start(projectDetailDTO.getStart())
                .end(projectDetailDTO.getEnd())
                .devStatus(projectDetailDTO.getDevStatus())
                .devType(projectDetailDTO.getDevType())
                .participants(projectDetailDTO.getParticipants())
                .links(projectDetailDTO.getLinks())
                .build();
    }
}
