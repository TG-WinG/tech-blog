package kr.tgwing.tech.user.service;


import kr.tgwing.tech.user.dto.LoginDTO;
import kr.tgwing.tech.user.dto.UserDTO;

public interface UserService{
    void register(UserDTO userDTO) throws Exception;

    UserDTO login(LoginDTO loginDTO);

    void logout(String token, String studentId);

}
