package kr.tgwing.tech.project.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.tgwing.tech.common.BaseEntity;
import kr.tgwing.tech.project.domain.Enum.DevRole;
import kr.tgwing.tech.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.validation.annotation.Validated;

@Entity
@Getter
@Builder
@DynamicInsert
@NoArgsConstructor
@Validated
@Table(name= "participant")
public class ParticipantEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id",nullable = false)
    private ProjectEntity project;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DevRole devRole;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity userEntity;

    @NotNull
    private String major;


    @Builder
    public ParticipantEntity(Long id, ProjectEntity project, DevRole devRole, UserEntity userEntity, String major) {
        this.id = id;
        this.project = project;
        this.devRole = devRole;
        this.userEntity = userEntity;
        this.major = major;
    }
}
