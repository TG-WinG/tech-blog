package kr.tgwing.tech.dto;

import kr.tgwing.tech.domain.project.ParticipantEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectBriefDTO {

    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private String thumbnail;
    private String devStatus;
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
