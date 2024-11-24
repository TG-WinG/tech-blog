package kr.tgwing.tech.project.dto;

import kr.tgwing.tech.project.domain.Enum.Part;
import kr.tgwing.tech.project.domain.Participant;
import kr.tgwing.tech.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantDTO {
    private Part part;
    private String studentNumber;
    private String username;
    private String major;
    private User user;

    @Builder
    public ParticipantDTO(Part part, String username, String major, String studentNumber, User user) {
        this.part = part;
        this.studentNumber = studentNumber;
        this.user = user;
        this.username = username;
        this.major = major;
    }


    public static Participant toParticipantEntity(ParticipantDTO participantDTO){
        return Participant.builder()
                .part(participantDTO.getPart())
                .studentNumber(participantDTO.getStudentNumber())
                .user(participantDTO.getUser())
                .name(participantDTO.getUsername())
                .major(participantDTO.getMajor())
                .build();
    }

}
