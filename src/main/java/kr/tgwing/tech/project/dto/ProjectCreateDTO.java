package kr.tgwing.tech.project.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import kr.tgwing.tech.project.domain.Enum.DevType;
import kr.tgwing.tech.project.domain.ProjectEntity;
import kr.tgwing.tech.project.domain.ThumbnailEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectCreateDTO {
    @NotNull
    @Length(min = 1, max = 25)
    private String title;

    @NotNull
    private String description;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    private List<ThumbnailDTO> thumbnailDTOS = new ArrayList<>();

    @NotNull
    @Length(min = 1, max = 25)
    private String devStatus;


    @NotNull
    private DevType devType;

    private List<ParticipantDTO> participantDTOS = new ArrayList<>();
    private List<LinkDTO> linkDTOS = new ArrayList<>();

    @Builder
    public ProjectCreateDTO(String title, String description, LocalDateTime start, LocalDateTime end, List<ThumbnailDTO> thumbnailDTOS, String devStatus, DevType devType, List<ParticipantDTO> participantDTOS, List<LinkDTO> linkDTOS) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.thumbnailDTOS = thumbnailDTOS;
        this.devStatus = devStatus;
        this.devType = devType;
        this.linkDTOS = linkDTOS;
        this.participantDTOS = participantDTOS;
    }


}
