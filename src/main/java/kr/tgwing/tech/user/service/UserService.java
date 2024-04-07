package kr.tgwing.tech.user.service;


import kr.tgwing.tech.user.dto.LoginDTO;
import kr.tgwing.tech.user.dto.ProfileReqDTO;
import kr.tgwing.tech.user.dto.UserDTO;

import java.security.Principal;

public interface UserService{
    void register(UserDTO userDTO) throws Exception;

    UserDTO login(LoginDTO loginDTO);

    void logout(String token, String studentId);

    UserDTO changeUser(String name, ProfileReqDTO request);

}
