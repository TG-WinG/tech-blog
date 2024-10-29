package kr.tgwing.tech.project.dto;

import kr.tgwing.tech.project.domain.Image;
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
public class ProjectCreateDTO {
    private String title;
    private String description;
    private LocalDate start;
    private LocalDate end;
    private List<String> imageUrls;
    private String devStatus;
    private String devType;
    private List<ParticipantDTO> participants = new ArrayList<>();
    private List<LinkDTO> links = new ArrayList<>();

    @Builder
    public ProjectCreateDTO(String title, String description, LocalDate start, LocalDate end,
                            List<String> imageUrls, String devStatus, String devType,
                            List<ParticipantDTO> participants, List<LinkDTO> links) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.imageUrls = imageUrls;
        this.devStatus = devStatus;
        this.devType = devType;
        this.links = links;
        this.participants = participants;
    }

    public Project toEntity(ProjectCreateDTO projectCreateDTO){

        return Project.builder()
                .title(projectCreateDTO.getTitle())
                .description(projectCreateDTO.getDescription())
                .start(projectCreateDTO.getStart())
                .end(projectCreateDTO.getEnd())
                .imageUrls(projectCreateDTO.getImageUrls().stream().map(
                        url -> Image.builder()
                                .imageUrl(url)
                                .build()
                ).toList())
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
