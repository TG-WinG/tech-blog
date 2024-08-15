package kr.tgwing.tech.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.tgwing.tech.blog.entity.PostEntity;
import kr.tgwing.tech.blog.repository.PostRepository;
import kr.tgwing.tech.user.dto.*;
import kr.tgwing.tech.user.dto.checkdto.CheckNumberDTO;
import kr.tgwing.tech.user.dto.checkdto.CheckUserDTO;
import kr.tgwing.tech.user.dto.checkdto.PasswordCheckDTO;
import kr.tgwing.tech.user.dto.profiledto.ProfileDTO;
import kr.tgwing.tech.user.dto.profiledto.ProfileReqDTO;
import kr.tgwing.tech.user.dto.registerdto.UserDTO;
import kr.tgwing.tech.user.entity.TempUser;
import kr.tgwing.tech.user.entity.User;
import kr.tgwing.tech.user.exception.*;
import kr.tgwing.tech.user.repository.TempUserRepository;
import kr.tgwing.tech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final PostRepository postRepository;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final SpringTemplateEngine templateEngine;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TempUserRepository tempUserRepository;

    @Override
    public Long register(UserDTO userDTO){
        Optional<User> user = userRepository.findByStudentId(userDTO.getStudentId());
        if(user.isPresent())
            throw new UserDuplicatedException();
        TempUser tempUser = UserDTO.toTempUser(userDTO);
        tempUser.hashPassword(bCryptPasswordEncoder);

        return tempUserRepository.save(tempUser).getId();
    }


    @Override
    public Long logout(String studentId) {
        User user = userRepository.findByStudentId(studentId).orElseThrow(UserNotFoundException::new);

        return user.getId();
    }

    // user 정보 수정하기
    @Override
    public Long changeUser(String studentId, ProfileReqDTO request){
        User userEntity = userRepository.findByStudentId(studentId)
                .orElseThrow(UserNotFoundException::new);
        userRepository.changeUser(studentId, request.getName(), request.getPhoneNumber(), request.getProfilePicture());
        Long id = userEntity.getId();
        return id;
    };

    @Override
    public Long removeUser(String studentId){
        userRepository.findByStudentId(studentId)
                .orElseThrow(UserNotFoundException::new); // user의 존재여부 확인
        userRepository.deleteByStudentId(studentId);
        return null;
    }

    @Override
    public ProfileDTO showUser(String studentId){
        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(UserNotFoundException::new);
        // 만약 사용자 정보가 존재한다면 업데이트를 수행
        ProfileDTO profileDTO = user.toProfileDTO(user);

        return profileDTO;
    }

    @Override
    public List<PostEntity> showMyBlog(String studentId){
        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(UserNotFoundException::new);
        List<PostEntity> myBlog = postRepository.findByWriter(user.getId());
        return myBlog;
    }

    @Override
    public void checkUser(CheckUserDTO checkUserDTO) {
        User user = userRepository.findByStudentId(checkUserDTO.getStudentId())
                .orElseThrow(UserNotFoundException::new);

        if(!user.getEmail().equals(checkUserDTO.getEmail()) || !user.getName().equals(checkUserDTO.getName()))
            throw new UserNotFoundException();
    }

    @Override
    public String sendEmail(EmailMessageDTO emailMessageDTO) {
        String authNum = createCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessageDTO.getReceiver());
            mimeMessageHelper.setSubject(emailMessageDTO.getSubject());
            mimeMessageHelper.setText(setContext(authNum, "email"), true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new MessageException();
        }

        return authNum;
    }

    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }

        return key.toString();
    }

    public String setContext(String code, String type) {
        org.thymeleaf.context.Context context = new Context();
        context.setVariable("code", code);

        return templateEngine.process("email", context);
    }

    @Override
    public Long setNewPassword(String studentId, PasswordCheckDTO password) {
        String newPassword = password.getNewPassword();

        if(newPassword.equals(password.getCheckPassword())) {
            User user = userRepository.findByStudentId(studentId.toString()).orElseThrow(UserNotFoundException::new);
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));

            return userRepository.save(user).getId();
        }
        else {
            throw new PasswordException();// 비밀번호가 서로 일치하지 않습니다.
        }
    }

    @Override
    public void checkCode(String code, CheckNumberDTO checkNumberDTO) {
        if(!code.equals(checkNumberDTO.getCode())) throw new EmailCodeException(); // 인증코드가 일치하지 않습니다.
    }
}
