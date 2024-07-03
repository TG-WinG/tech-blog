package kr.tgwing.tech.project.dto;

import jakarta.validation.constraints.NotNull;
import kr.tgwing.tech.project.domain.Enum.DevType;
import kr.tgwing.tech.project.domain.ThumbnailEntity;
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
    @NotNull
    private Long project_id;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private List<ThumbnailEntity> thumbnails = new ArrayList<>();
    @NotNull
    private String devStatus;
    @NotNull
    private DevType devType;

    @Builder
    public ProjectBriefDTO(Long project_id, String title, String description, List<ThumbnailEntity> thumbnails, String devStatus, DevType devType) {
        this.project_id = project_id;
        this.title = title;
        this.description = description;
        this.thumbnails = thumbnails;
        this.devStatus = devStatus;
        this.devType = devType;
    }
}
