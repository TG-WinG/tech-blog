package kr.tgwing.tech.project.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.tgwing.tech.common.BaseEntity;
import kr.tgwing.tech.project.domain.Enum.DevRole;
import kr.tgwing.tech.user.entity.OutsiderEntity;
import lombok.*;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor
@Table(name = "outsider_participant")
public class OutsiderParticipantEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="outsider_participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id",nullable = false)
    private ProjectEntity project;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DevRole devRole;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="outsider_id")
    private OutsiderEntity outsiderEntity;

    @NotNull
    private String major;


    @Builder
    public OutsiderParticipantEntity(Long id, ProjectEntity project, DevRole devRole, OutsiderEntity outsiderEntity, String major) {
        this.id = id;
        this.project = project;
        this.devRole = devRole;
        this.outsiderEntity = outsiderEntity;
        this.major = major;
    }
}
