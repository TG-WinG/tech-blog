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
public class ProjectDetailDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private String thumbnail;
    private String devStatus;
    private String devType;

    // 참여인원에 대해 ParticipateEntity;
    private List<ParticipantEntity> participants = new ArrayList<>();
    // link
    private List<LinkEntity> links = new ArrayList<>();

    @Builder
    public ProjectDetailDTO(Long id, String title, String description, LocalDateTime start, LocalDateTime end, String thumbnail, String devStatus, String devType, List<ParticipantEntity> participants, List<LinkEntity> links) {
        this.id = id;
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
                .id(projectDetailDTO.getId())
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
