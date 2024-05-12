package kr.tgwing.tech.user.service;


import kr.tgwing.tech.user.dto.*;

public interface UserService{
    void register(UserDTO userDTO) throws Exception;

    void logout(String token, String studentId);

    Long changeUser(String name, ProfileReqDTO request);

    ProfileDTO showUser(String name);

    Boolean checkUser(CheckUserDTO checkUserDTO); // 본인 확인하기

    String sendEmail(EmailMessageDTO emailMessage); // 메일로 인증번호 전송하기

    void setNewPassword(Object studentId, PasswordCheckDTO password);

}
