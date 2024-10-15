package kr.tgwing.tech.user.service;


import java.util.List;

import kr.tgwing.tech.blog.dto.PostOverview;
import kr.tgwing.tech.blog.entity.Post;
import kr.tgwing.tech.user.dto.EmailMessageDTO;
import kr.tgwing.tech.user.dto.checkdto.CheckNumberDTO;
import kr.tgwing.tech.user.dto.checkdto.CheckUserDTO;
import kr.tgwing.tech.user.dto.checkdto.PasswordCheckDTO;
import kr.tgwing.tech.user.dto.profiledto.ProfileDTO;
import kr.tgwing.tech.user.dto.profiledto.ProfileReqDTO;
import kr.tgwing.tech.user.dto.registerdto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService{

    Long register(UserDTO userDTO);

    Long logout(String studentNumber);

    Long changeUser(String name, ProfileReqDTO request);

    Long removeUser(String name);


    ProfileDTO showUser(String name);

    Page<PostOverview> showMyBlog(String studentNumber, Pageable pageable);

    void checkUser(CheckUserDTO checkUserDTO); // 본인 확인하기

    String sendEmail(EmailMessageDTO emailMessage); // 메일로 인증번호 전송하기

    Long setNewPassword(String studentNumber, PasswordCheckDTO password);

    void checkCode(String code, CheckNumberDTO checkNumberDTO);
}
