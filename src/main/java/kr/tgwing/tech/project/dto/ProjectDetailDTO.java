package kr.tgwing.tech.project.dto;

import jakarta.validation.constraints.NotNull;
import kr.tgwing.tech.project.domain.*;
import kr.tgwing.tech.project.domain.Enum.DevType;
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
    private Long project_id;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    @NotNull
    private List<ThumbnailEntity> thumbnails = new ArrayList<>();
    @NotNull
    private String devStatus;
    @NotNull
    private DevType devType;
    @NotNull
    private List<OutsiderParticipantEntity> outsiderParticipants = new ArrayList<>();
    @NotNull
    private List<ParticipantEntity> participants = new ArrayList<>();
    @NotNull
    private List<LinkEntity> links = new ArrayList<>();

    @Builder
    public ProjectDetailDTO(Long project_id, String title, String description, LocalDateTime start, LocalDateTime end, List<ThumbnailEntity> thumbnails, String devStatus, DevType devType, List<OutsiderParticipantEntity> outsiderParticipants,List<ParticipantEntity> participants, List<LinkEntity> links) {
        this.project_id = project_id;
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.thumbnails = thumbnails;
        this.devStatus = devStatus;
        this.devType = devType;
        this.participants = participants;
        this.outsiderParticipants = outsiderParticipants;
        this.links = links;
    }

    public static ProjectEntity toEntity(ProjectDetailDTO projectDetailDTO){
        return ProjectEntity.builder()
                .title(projectDetailDTO.getTitle())
                .description(projectDetailDTO.getDescription())
                .start(projectDetailDTO.getStart())
                .end(projectDetailDTO.getEnd())
                .thumbnails(projectDetailDTO.getThumbnails())
                .devStatus(projectDetailDTO.getDevStatus())
                .devType(projectDetailDTO.getDevType())
                .participants(projectDetailDTO.getParticipants())
                .outsiderParticipants(projectDetailDTO.getOutsiderParticipants())
                .links(projectDetailDTO.getLinks())
                .build();
    }
}
