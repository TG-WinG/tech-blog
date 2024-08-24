package kr.tgwing.tech.project.domain;


import jakarta.persistence.*;
import kr.tgwing.tech.common.BaseEntity;
import kr.tgwing.tech.project.dto.LinkDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import jakarta.persistence.Entity;


@Getter
@DynamicInsert
@NoArgsConstructor
@Table(name= "link")
@Entity
public class Link extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="link_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;

    @Column(name = "link_url")
    private String url;

    private String description;

    public void setProject(Project project) {
        this.project = project;
    }

    @Builder
    public Link(Long id, Project project, String url, String description) {
        this.id = id;
        this.project = project;
        this.url = url;
        this.description = description;
    }

    public static LinkDTO toDTO(Link link){
        return LinkDTO.builder()
                .description(link.getDescription())
                .url(link.getUrl())
                .build();
    }


}
