package kr.tgwing.tech.user.service;

import kr.tgwing.tech.user.dto.LoginDTO;
import kr.tgwing.tech.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Override
    public void register(UserDTO userDTO) {

    }

    @Override
    public UserDTO login(LoginDTO loginDTO) {

        UserDTO userDTO = new UserDTO();

        return userDTO;
    }

    @Override
    public void logout(String token, String studentId) {

    }

}
