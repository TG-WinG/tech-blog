package kr.tgwing.tech.dto;

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
public class ProjectCreateDTO {
    private String title;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private String thumbnail;
    private String devStatus;
    private String devType;
    private List<ParticipantDTO> participants = new ArrayList<>();
    private List<LinkDTO> links = new ArrayList<>();

    @Builder
    public ProjectCreateDTO(String title, String description, LocalDateTime start, LocalDateTime end, String thumbnail, String devStatus, String devType, List<ParticipantDTO> participants, List<LinkDTO> links) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.thumbnail = thumbnail;
        this.devStatus = devStatus;
        this.devType = devType;
        this.links = links;
        this.participants = participants;
    }

    public static ProjectEntity toEntity(ProjectCreateDTO projectCreateDTO){

        return ProjectEntity.builder()
                .title(projectCreateDTO.getTitle())
                .description(projectCreateDTO.getDescription())
                .start(projectCreateDTO.getStart())
                .end(projectCreateDTO.getEnd())
                .thumbnail(projectCreateDTO.getThumbnail())
                .devStatus(projectCreateDTO.getDevStatus())
                .devType(projectCreateDTO.getDevType())
                .links(projectCreateDTO.getLinks().stream()
                        .map(LinkDTO::toLinkEntity)
                        .toList())
                .participants(projectCreateDTO.getParticipants().stream()
                        .map(ParticipantDTO::toParticipantEntity)
                        .toList())
                .build();
    }
}
