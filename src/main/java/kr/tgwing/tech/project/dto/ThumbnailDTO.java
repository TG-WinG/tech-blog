package kr.tgwing.tech.project.dto;

import jakarta.validation.constraints.NotNull;
import kr.tgwing.tech.project.domain.ProjectEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThumbnailDTO {
    @NotNull
    private String url;
    @NotNull
    private ProjectEntity project;

    @Builder
    public ThumbnailDTO(String url, ProjectEntity project) {
        this.url = url;
        this.project = project;
    }


}
