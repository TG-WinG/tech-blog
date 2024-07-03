package kr.tgwing.tech.admin.service;

import kr.tgwing.tech.admin.dto.AdminCheckUserDto;
import kr.tgwing.tech.user.entity.UserEntity;
import kr.tgwing.tech.user.exception.UserNotFoundException;
import kr.tgwing.tech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl {

    private final UserRepository userRepository;

    public List<AdminCheckUserDto> checkUser() {
        List<UserEntity> waitingUsers = userRepository.findWaitingMember();

        if(waitingUsers.isEmpty())
            return null; // 여기 값은 예외로 보내야하느건지, 어떻게 보내야하는거지?

        List<AdminCheckUserDto> dtoList = new ArrayList<>();

        for(UserEntity user : waitingUsers) {
            AdminCheckUserDto dto = AdminCheckUserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .studentId(user.getStudentId())
                    .phoneNumber(user.getPhoneNumber())
                    .build();

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Transactional
    public Long registerUsers(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        user.setRole("ROLE_USER");
        return user.getId();
    }

    public Long refuseUsers(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(user.getId());

        return user.getId();
    }
}
