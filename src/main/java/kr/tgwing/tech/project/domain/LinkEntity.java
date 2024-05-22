package kr.tgwing.tech.project.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.tgwing.tech.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import jakarta.persistence.Entity;
import org.springframework.validation.annotation.Validated;


@Getter
@Builder
@DynamicInsert
@NoArgsConstructor
@Table(name= "link")
@Entity
@Validated
public class LinkEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="link_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id")
    private ProjectEntity project;

    @NotNull
    private String url;

    private String description;


    @Builder
    public LinkEntity(Long id, ProjectEntity project, String url, String description) {
        this.id = id;
        this.project = project;
        this.url = url;
        this.description = description;
    }

    public void updateLink(ProjectEntity project, String url, String description){
        this.project = project;
        this.url = url;
        this.description = description;
    }


}
