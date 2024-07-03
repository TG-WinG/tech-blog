package kr.tgwing.tech.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ParticipantEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id",nullable = false)
    @JsonIgnore
    private ProjectEntity project;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DevRole devRole;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private UserEntity userEntity;

    @NotNull
    private String major;

    @NotNull
    private String name;


    @Builder
    public ParticipantEntity(Long id, ProjectEntity project, DevRole devRole, UserEntity userEntity, String major, String name) {
        this.id = id;
        this.project = project;
        this.devRole = devRole;
        this.userEntity = userEntity;
        this.major = major;
        this.name = name;
    }
}
