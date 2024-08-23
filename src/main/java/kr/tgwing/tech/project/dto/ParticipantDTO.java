package kr.tgwing.tech.project.dto;

import kr.tgwing.tech.project.domain.Enum.Part;
import kr.tgwing.tech.project.domain.Participant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantDTO {
    private Part part;
    private String username;
    private String major;

    @Builder
    public ParticipantDTO(Part part, String username, String major) {
        this.part = part;
        this.username = username;
        this.major = major;
    }

    public static Participant toParticipantEntity(ParticipantDTO participantDTO){
        return Participant.builder()
                .part(participantDTO.getPart())
                .name(participantDTO.getUsername())
                .major(participantDTO.getMajor())
                .build();
    }

}
