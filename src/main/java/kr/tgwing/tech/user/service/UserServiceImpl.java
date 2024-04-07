package kr.tgwing.tech.user.service;

import kr.tgwing.tech.user.dto.LoginDTO;
import kr.tgwing.tech.user.dto.UserDTO;
import kr.tgwing.tech.user.entity.UserEntity;
import kr.tgwing.tech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void register(UserDTO userDTO) throws Exception{
        String username = userDTO.getName();
        String password = userDTO.getPassword();

        Boolean isExist = userRepository.existsByName(username);

        if (isExist) {

            return;
        }

        UserEntity data = UserDTO.toUserEntity(userDTO);

        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setRole("ROLE_USER");

        userRepository.save(data);
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
