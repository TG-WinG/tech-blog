package kr.tgwing.tech.user.service;


import kr.tgwing.tech.user.dto.LoginDTO;
import kr.tgwing.tech.user.dto.ProfileDTO;
import kr.tgwing.tech.user.dto.ProfileReqDTO;
import kr.tgwing.tech.user.dto.UserDTO;

import java.security.Principal;

public interface UserService{
    void register(UserDTO userDTO) throws Exception;

    void logout(String token, String studentId);

    Long changeUser(String name, ProfileReqDTO request);

    ProfileDTO showUser(String name);

}
