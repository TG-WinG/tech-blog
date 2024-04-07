package kr.tgwing.tech.domain.project;

import jakarta.persistence.*;
import kr.tgwing.tech.domain.Enum.DevRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Builder
@DynamicInsert
@NoArgsConstructor
@Table(name= "participant")
public class ParticipantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="participant_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="project_id")
    private ProjectEntity project;

    @Enumerated(EnumType.STRING)
    private DevRole devRole;

    private String username;

    private String major;

    // project create 할 떄 한번 썻읍니다..허허
    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    @Builder
    public ParticipantEntity(Long id, ProjectEntity project, DevRole devRole, String username, String major) {
        this.id = id;
        this.project = project;
        this.devRole = devRole;
        this.username = username;
        this.major = major;
    }
}
