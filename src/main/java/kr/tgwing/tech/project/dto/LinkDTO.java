package kr.tgwing.tech.project.dto;

import jakarta.validation.constraints.NotNull;
import kr.tgwing.tech.project.domain.LinkEntity;
import kr.tgwing.tech.project.domain.ProjectEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LinkDTO {
    @NotNull
    private String url;
    @NotNull
    private String description;
    @NotNull
    private ProjectEntity project;

    @Builder
    public LinkDTO(String url, String description, ProjectEntity project) {
        this.url = url;
        this.description = description;
        this.project = project;
    }

}
