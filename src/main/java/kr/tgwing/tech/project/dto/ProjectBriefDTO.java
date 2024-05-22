package kr.tgwing.tech.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectBriefDTO {
    @NotNull
    private String title;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    @NotNull
    private String thumbnail;
    @NotNull
    private String devStatus;
    @NotNull
    private String devType;

    @Builder
    public ProjectBriefDTO(String title, LocalDateTime start, LocalDateTime end, String thumbnail, String devStatus, String devType) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.thumbnail = thumbnail;
        this.devStatus = devStatus;
        this.devType = devType;
    }
}
