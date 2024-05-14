package kr.tgwing.tech.user.service;


import kr.tgwing.tech.user.dto.*;

public interface UserService{
    Long register(UserDTO userDTO);

    Long logout(String studentId);

    Long changeUser(String name, ProfileReqDTO request);

    Long removeUser(String name);

    ProfileDTO showUser(String name);

    Boolean checkUser(CheckUserDTO checkUserDTO); // 본인 확인하기

    String sendEmail(EmailMessageDTO emailMessage); // 메일로 인증번호 전송하기

    Long setNewPassword(Object studentId, PasswordCheckDTO password);


}
