package kr.tgwing.tech.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name= "thumbnail")
@Entity
@Validated
public class ThumbnailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="thumbnail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name="project_id")
    private ProjectEntity project;

    @NotNull
    private String url;

    @Builder
    public ThumbnailEntity(Long id, ProjectEntity project, String url) {
        this.id = id;
        this.project = project;
        this.url = url;
    }
    public void updateThumbnail(ProjectEntity project, String url){
        this.project = project;
        this.url = url;
    }



}