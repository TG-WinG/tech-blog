package kr.tgwing.tech.user.service;

import kr.tgwing.tech.user.dto.LoginDTO;
import kr.tgwing.tech.user.dto.ProfileDTO;
import kr.tgwing.tech.user.dto.ProfileReqDTO;
import kr.tgwing.tech.user.dto.UserDTO;
import kr.tgwing.tech.user.entity.UserEntity;
import kr.tgwing.tech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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
}
