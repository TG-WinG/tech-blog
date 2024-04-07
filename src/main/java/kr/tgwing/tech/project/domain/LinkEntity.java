package kr.tgwing.tech.project.domain;


import jakarta.persistence.*;
import kr.tgwing.tech.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import jakarta.persistence.Entity;


@Getter
@Builder
@DynamicInsert
@NoArgsConstructor
@Table(name= "link")
@Entity
public class LinkEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="link_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="project_id")
    private ProjectEntity project;

    private String url;

    private String description;

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    @Builder
    public LinkEntity(Long id, ProjectEntity project, String url, String description) {
        this.id = id;
        this.project = project;
        this.url = url;
        this.description = description;
    }


}
