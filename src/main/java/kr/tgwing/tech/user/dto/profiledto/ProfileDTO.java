package kr.tgwing.tech.user.dto.profiledto;

import java.time.LocalDate;

import kr.tgwing.tech.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    private String studentNumber;
    private String email;
    private String name; // 이름
    private LocalDate birth;
    private String phoneNumber;
    private String profilePicture;

    public static ProfileDTO of(User user) {
        return ProfileDTO.builder()
                .studentNumber(user.getStudentNumber())
                .email(user.getEmail())
                .name(user.getName())
                .birth(user.getBirth())
                .phoneNumber(user.getPhoneNumber())
                .profilePicture(user.getProfilePicture())
                .build();
    }
}
