package kr.tgwing.tech.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.tgwing.tech.user.dto.*;
import kr.tgwing.tech.user.entity.UserEntity;
import kr.tgwing.tech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void register(UserDTO userDTO) throws Exception{
        String studentId = userDTO.getStudentId();
        String password = userDTO.getPassword();

        Boolean isExist = userRepository.existsByStudentId(studentId);

        if (isExist) {
            return;
        }

        UserEntity data = UserDTO.toUserEntity(userDTO);

        data.setPassword(bCryptPasswordEncoder.encode(password)); // 비밀번호 암호화
        data.setRole("ROLE_USER"); // register를 통해서 회원가입하는 유저들은 모두 USER역할

        userRepository.save(data);
    }


    @Override
    public void logout(String token, String studentId) {

    }


    @Override
    public Long changeUser(String studentId, ProfileReqDTO request){
        Optional<UserEntity> userEntity = userRepository.findByStudentId(studentId);
        // 만약 사용자 정보가 존재한다면 업데이트를 수행
        if (userEntity.isPresent()) {
            userRepository.changeUser(studentId, request.getName(), request.getPhoneNumber(), request.getProfilePicture());
        }

        Optional<UserEntity> byStudentId = userRepository.findByStudentId(studentId);
        Long id = byStudentId.get().getId();
        // 업데이트된 사용자 정보가 존재한다면 UserDTO로 변환하여 반환
        if (byStudentId.isPresent()) {
            return id;
        }
        else{
            return null;
        }
    };

    @Override
    public ProfileDTO showUser(String studentId){
        Optional<UserEntity> byStudentId = userRepository.findByStudentId(studentId);
        // 만약 사용자 정보가 존재한다면 업데이트를 수행
        if (byStudentId.isPresent()) {
            UserEntity user = byStudentId.get();

            // UserEntity를 UserDTO로 변환
            ProfileDTO userDTO = ProfileDTO.builder()
                    .studentId(user.getStudentId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .birth(user.getBirth())
                    .phoneNumber(user.getPhoneNumber())
                    .profilePicture(user.getProfilePicture())
                    .build();

            return userDTO;
        }

        else{
            return null;
        }
    }

    @Override
    public Boolean checkUser(CheckUserDTO checkUserDTO) {
        Optional<UserEntity> user = userRepository.findByStudentId(checkUserDTO.getStudentId());
        if(user.isPresent()) {
            if(user.get().getEmail().equals(checkUserDTO.getEmail()) && user.get().getName().equals(checkUserDTO.getName()))
                return true;
        }

        return false;
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
            throw new RuntimeException(e);
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
    public void setNewPassword(Object studentId, PasswordCheckDTO password) {
        String newPassword = password.getNewPassword();
        System.out.println("newPassword = " + newPassword);
        System.out.println("studentId.toString() = " + studentId.toString());

        if(newPassword.equals(password.getCheckPassword())) {
            Optional<UserEntity> user = userRepository.findByStudentId(studentId.toString());
            System.out.println("user = " + user);

            if(user.isPresent()) {
                user.get().setPassword(bCryptPasswordEncoder.encode(newPassword));
                userRepository.save(user.get());
            }
            else {
                // 비밀번호 불일치함.
            }

        }
        else {
            throw new IllegalStateException(); // 비밀번호가 서로 일치하지 않습니다.
        }
    }

}
