package kr.tgwing.tech.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.tgwing.tech.common.BaseEntity;
import kr.tgwing.tech.project.domain.OutsiderParticipantEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor
@Table(name = "outsider")
public class OutsiderEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String studentId;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "outsiderEntity",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OutsiderParticipantEntity> outsiderParticipants = new ArrayList<>();

    public OutsiderEntity(Long id, String studentId, String name, List<OutsiderParticipantEntity> outsiderParticipants) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.outsiderParticipants = outsiderParticipants;
    }

    public void updateName(String name){
        this.name = name;
    }
}

