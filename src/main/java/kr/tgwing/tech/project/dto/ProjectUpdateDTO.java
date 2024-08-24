package kr.tgwing.tech.project.dto;

import kr.tgwing.tech.project.domain.Link;
import kr.tgwing.tech.project.domain.Participant;
import kr.tgwing.tech.project.domain.Project;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectUpdateDTO {
    private String title;
    private String description;
    private LocalDate start;
    private LocalDate end;
    private String thumbnail;
    private String devStatus;
    private String devType;
    private List<ParticipantDTO> participants = new ArrayList<>();
    private List<LinkDTO> links = new ArrayList<>();

    @Builder
    public ProjectUpdateDTO(String title, String description, LocalDate start, LocalDate end, String thumbnail, String devStatus, String devType, List<ParticipantDTO> participants, List<LinkDTO> links) {
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

    public static Project toEntity(ProjectUpdateDTO projectUpdateDTO){
        return Project.builder()
                .title(projectUpdateDTO.getTitle())
                .description(projectUpdateDTO.getDescription())
                .start(projectUpdateDTO.getStart())
                .end(projectUpdateDTO.getEnd())
                .devStatus(projectUpdateDTO.getDevStatus())
                .devType(projectUpdateDTO.getDevType())
                .participants(projectUpdateDTO.getParticipants().stream()
                        .map(ParticipantDTO::toParticipantEntity).toList())
                .links(projectUpdateDTO.getLinks().stream()
                        .map(LinkDTO::toLinkEntity).toList())
                .build();
    }
}
