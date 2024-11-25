package kr.tgwing.tech.project.dto;

import kr.tgwing.tech.project.domain.Image;
import kr.tgwing.tech.project.domain.Project;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectBriefDTO {
    private Long id;
    private String title;
    private LocalDate start;
    private LocalDate end;
    private String description;
    private String thumbnail;
    private String devStatus;
    private String devType;

    @Builder
    public ProjectBriefDTO(Long id, String title, LocalDate start, LocalDate end, String description, String thumbnail, String devStatus, String devType) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.description = description;
        this.thumbnail = thumbnail;
        this.devStatus = devStatus;
        this.devType = devType;
    }

    public static ProjectBriefDTO of(Project project) {
        String imgUrl;
        Optional<Image> first = project.getImageUrls().stream().findFirst();
        if (first.isEmpty()) {
            imgUrl = null;
        } else {
            imgUrl = first.get().getImageUrl();
        }
        return ProjectBriefDTO.builder()
                .id(project.getId())
                .title(project.getTitle())
                .start(project.getStartDate())
                .end(project.getEndDate())
                .description(project.getDescription())
                .thumbnail(imgUrl)
                .devStatus(project.getDevStatus())
                .devType(project.getDevType())
                .build();
    }
}
