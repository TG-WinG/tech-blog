package kr.tgwing.tech.project.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectBriefDTO {
    private Long id;
    private String title;
    private LocalDate start;
    private LocalDate end;
    private String thumbnail;
    private String devStatus;
    private String devType;

    @Builder
    public ProjectBriefDTO(Long id, String title, LocalDate start, LocalDate end, String thumbnail, String devStatus, String devType) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.thumbnail = thumbnail;
        this.devStatus = devStatus;
        this.devType = devType;
    }
}
