package kr.tgwing.tech.project.domain;

import jakarta.persistence.*;
import kr.tgwing.tech.common.BaseEntity;
import kr.tgwing.tech.project.domain.Enum.Part;
import kr.tgwing.tech.project.dto.ParticipantDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor
@Table(name= "participant")
public class Participant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="participant_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    private Part part;

    private String name;

    private String major;

    public void setProject(Project project) {
        this.project = project;
    }

    public static ParticipantDTO toDTO(Participant participant) {
        return ParticipantDTO.builder()
                .username(participant.getName())
                .major(participant.getMajor())
                .part(participant.getPart())
                .build();
    }

    @Builder
    public Participant(Long id, Project project, Part part, String name, String major) {
        this.id = id;
        this.project = project;
        this.part = part;
        this.name = name;
        this.major = major;
    }
}
