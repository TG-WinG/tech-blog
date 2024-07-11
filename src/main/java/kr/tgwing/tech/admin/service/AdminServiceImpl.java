package kr.tgwing.tech.admin.service;

import kr.tgwing.tech.admin.dto.AdminCheckUserDto;
import kr.tgwing.tech.user.entity.TempUser;
import kr.tgwing.tech.user.entity.User;
import kr.tgwing.tech.user.exception.UserNotFoundException;
import kr.tgwing.tech.user.repository.TempUserRepository;
import kr.tgwing.tech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl {

    private final UserRepository userRepository;
    private final TempUserRepository tempUserRepository;

    public List<AdminCheckUserDto> checkUser() {
        List<TempUser> allUser = tempUserRepository.findAll();
        if(allUser.isEmpty()) return null; // 여기 값은 예외로 보내야하느건지, 어떻게 보내야하는거지?

        List<AdminCheckUserDto> dtoList = new ArrayList<>();
        for(TempUser user : allUser) {
            AdminCheckUserDto dto = user.toAdminCheckUserDto(user);
            dtoList.add(dto);
        }

        return dtoList;
    }

    @Transactional
    public Long registerUsers(Long id) {
        TempUser notUser = tempUserRepository.findById(id).orElseThrow(UserNotFoundException::new);
        User user = notUser.toUser(notUser);
        tempUserRepository.deleteById(id);

        user.setRole("ROLE_USER");
        userRepository.save(user);

        return user.getId();
    }

    public Long refuseUsers(Long id) {
        TempUser user = tempUserRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(user.getId());

        return user.getId();
    }
}
