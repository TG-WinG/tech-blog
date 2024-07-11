package kr.tgwing.tech.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.tgwing.tech.blog.entity.PostEntity;
import kr.tgwing.tech.blog.repository.PostRepository;
import kr.tgwing.tech.user.dto.*;
import kr.tgwing.tech.user.dto.checkdto.CheckUserDTO;
import kr.tgwing.tech.user.dto.checkdto.PasswordCheckDTO;
import kr.tgwing.tech.user.dto.profiledto.ProfileDTO;
import kr.tgwing.tech.user.dto.profiledto.ProfileReqDTO;
import kr.tgwing.tech.user.dto.registerdto.UserDTO;
import kr.tgwing.tech.user.entity.UserEntity;
import kr.tgwing.tech.user.exception.MessageException;
import kr.tgwing.tech.user.exception.PasswordException;
import kr.tgwing.tech.user.exception.UserDuplicatedException;
import kr.tgwing.tech.user.exception.UserNotFoundException;
import kr.tgwing.tech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PostRepository postRepository;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final SpringTemplateEngine templateEngine;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Long register(UserDTO userDTO){
        String studentId = userDTO.getStudentId();
        String password = userDTO.getPassword();

        Optional<UserEntity> user = userRepository.findByStudentId(studentId);

        if(user.isPresent())
            throw new UserDuplicatedException();

        UserEntity data = UserDTO.toUserEntity(userDTO);

        UserEntity save = userRepository.save(data);

        return save.getId();
    }


    @Override
    public Long logout(String studentId) {
        UserEntity user = userRepository.findByStudentId(studentId).orElseThrow(UserNotFoundException::new);

        return user.getId();
    }

    // user 정보 수정하기
    @Override
    public Long changeUser(String studentId, ProfileReqDTO request){

        UserEntity userEntity = userRepository.findByStudentId(studentId)
                .orElseThrow(UserNotFoundException::new);

        userRepository.changeUser(studentId, request.getName(), request.getPhoneNumber(), request.getProfilePicture());

        Long id = userEntity.getId();

        return id;
    };

    @Override
    public Long removeUser(String studentId){
        userRepository.deleteByStudentId(studentId);
        return null;
    }

    @Override
    public UserEntity getUserEntity(String studentId) {
        UserEntity getId = userRepository.getEntity(studentId);
        return getId;
    }


    @Override
    public ProfileDTO showUser(String studentId){

        UserEntity user = userRepository.findByStudentId(studentId)
                .orElseThrow(UserNotFoundException::new);
        // 만약 사용자 정보가 존재한다면 업데이트를 수행

        ProfileDTO profileDTO = ProfileDTO.builder()
                .studentId(user.getStudentId())
                .email(user.getEmail())
                .name(user.getName())
                .birth(user.getBirth())
                .phoneNumber(user.getPhoneNumber())
                .profilePicture(user.getProfilePicture())
                .build();

        return profileDTO;
    }

    @Override
    public List<PostEntity> showMyBlog(String studentId){
        UserEntity id = userRepository.getEntity(studentId);
        Long userId = id.getId();
        System.out.println(userId);
        List<PostEntity> myBlog = postRepository.findByWriter(userId);

        return myBlog;
    }

    @Override
    public Boolean checkUser(CheckUserDTO checkUserDTO) {
        UserEntity user = userRepository.findByStudentId(checkUserDTO.getStudentId())
                .orElseThrow(UserNotFoundException::new);

        if(user.getEmail().equals(checkUserDTO.getEmail()) && user.getName().equals(checkUserDTO.getName())) return true;

        else throw new UserNotFoundException();
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
    public Long setNewPassword(Object studentId, PasswordCheckDTO password) {
        String newPassword = password.getNewPassword();

        if(newPassword.equals(password.getCheckPassword())) {
            UserEntity user = userRepository.findByStudentId(studentId.toString()).orElseThrow(UserNotFoundException::new);

            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            UserEntity save = userRepository.save(user);

            return save.getId();
        }
        else {
            throw new PasswordException();// 비밀번호가 서로 일치하지 않습니다.
        }
    }

}
