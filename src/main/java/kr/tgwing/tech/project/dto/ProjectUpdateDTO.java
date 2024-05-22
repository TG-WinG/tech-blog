package kr.tgwing.tech.project.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import kr.tgwing.tech.project.domain.LinkEntity;
import kr.tgwing.tech.project.domain.ParticipantEntity;
import kr.tgwing.tech.project.domain.ProjectEntity;
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
public class ProjectUpdateDTO {

    @Length(min = 1, max = 25)
    private String title;


    private String description;


    private LocalDateTime start;


    private LocalDateTime end;

    private String thumbnail;


    @Length(max = 25)
    private String devStatus;

    @Length(max = 25)
    private String devType;

    private List<ParticipantDTO> participantDTOS = new ArrayList<>();

    private List<LinkDTO> linkDTOS = new ArrayList<>();

    @Builder
    public ProjectUpdateDTO(String title, String description, LocalDateTime start, LocalDateTime end, String thumbnail, String devStatus, String devType, List<ParticipantDTO> participantDTOS, List<LinkDTO> linkDTOS) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.thumbnail = thumbnail;
        this.devStatus = devStatus;
        this.devType = devType;
        this.linkDTOS = linkDTOS;
        this.participantDTOS = participantDTOS;
    }


}
