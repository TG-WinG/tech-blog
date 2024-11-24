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
public class ProjectDetailDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate start;
    private LocalDate end;
    private String devStatus;
    private String devType;
    private List<String> imageUrls = new ArrayList<>();
    private List<ParticipantDTO> participants = new ArrayList<>();
    private List<LinkDTO> links = new ArrayList<>();

    @Builder
    public ProjectDetailDTO(Long id, String title, String description,
                            LocalDate start, LocalDate end, String devStatus, String devType,
                            List<String> imageUrls, List<ParticipantDTO> participants, List<LinkDTO> links) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.imageUrls = imageUrls;
        this.devStatus = devStatus;
        this.devType = devType;
        this.participants = participants;
        this.links = links;
    }

    public static Project toEntity(ProjectDetailDTO projectDetailDTO){
        return Project.builder()
                .id(projectDetailDTO.getId())
                .title(projectDetailDTO.getTitle())
                .description(projectDetailDTO.getDescription())
                .start(projectDetailDTO.getStart())
                .end(projectDetailDTO.getEnd())
                .devStatus(projectDetailDTO.getDevStatus())
                .devType(projectDetailDTO.getDevType())
                .participants(projectDetailDTO.getParticipants().stream()
                        .map(ParticipantDTO::toParticipantEntity)
                        .toList())
                .links(projectDetailDTO.getLinks().stream()
                        .map(LinkDTO::toLinkEntity)
                        .toList())
                .build();
    }
}
