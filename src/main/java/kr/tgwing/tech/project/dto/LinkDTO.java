package kr.tgwing.tech.project.dto;

import kr.tgwing.tech.project.domain.project.LinkEntity;
import kr.tgwing.tech.project.domain.project.ProjectEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LinkDTO {
    private String url;
    private String description;
    private ProjectEntity project;

    @Builder
    public LinkDTO(String url, String description, ProjectEntity project) {
        this.url = url;
        this.description = description;
        this.project = project;
    }

    public static LinkEntity toLinkEntity(LinkDTO linkDTO){
        return LinkEntity.builder()
                .url(linkDTO.getUrl())
                .description(linkDTO.getDescription())
                .project(linkDTO.getProject())
                .build();
    }
}
