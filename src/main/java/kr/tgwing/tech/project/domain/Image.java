package kr.tgwing.tech.project.domain;

import jakarta.persistence.*;
import kr.tgwing.tech.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "Image")
@Getter
@NoArgsConstructor
@DynamicInsert
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "image_url")
    private String imageUrl;

    @Builder
    public Image (Project project, String imageUrl) {
        this.project = project;
        this.imageUrl = imageUrl;
    }

    public static String toDto(Image image) {
        return image.imageUrl.toString();
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
