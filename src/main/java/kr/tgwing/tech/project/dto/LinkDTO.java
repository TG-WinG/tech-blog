package kr.tgwing.tech.project.dto;

import kr.tgwing.tech.project.domain.Link;
import kr.tgwing.tech.project.domain.Project;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LinkDTO {
    private String url;
    private String description;

    @Builder
    public LinkDTO(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public static Link toLinkEntity(LinkDTO linkDTO){
        return Link.builder()
                .url(linkDTO.getUrl())
                .description(linkDTO.getDescription())
                .build();
    }
}
