package kr.tgwing.tech.user.service;

import kr.tgwing.tech.user.dto.LoginDTO;
import kr.tgwing.tech.user.dto.ProfileReqDTO;
import kr.tgwing.tech.user.dto.UserDTO;
import kr.tgwing.tech.user.entity.UserEntity;
import kr.tgwing.tech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public void register(UserDTO userDTO) {

    }

    @Override
    public UserDTO login(LoginDTO loginDTO) {

        String studentId = loginDTO.getStudentId();
        String password = loginDTO.getPassword();

        // UserRepository를 통해 데이터베이스에서 사용자 정보 조회
        UserEntity userEntity = userRepository.findByStudentId(studentId);

//        if (userEntity != null && userEntity.getPassword().equals(password)) {
//            //성공했을 경우 반환값?
//            return
//        }

        // 인증 실패 시 null 반환
        return null;
    }

    @Override
    public void logout(String token, String studentId) {

    }


    @Override
    public UserDTO changeUser(String name, ProfileReqDTO request){
        UserEntity user = userRepository.findByStudentId(name);
        // 만약 사용자 정보가 존재한다면 업데이트를 수행
        if (user != null) {
            userRepository.changeUser(name, request.getName(), request.getPhoneNumber(), request.getProfilePicture());
        }

        UserEntity updatedUser = userRepository.findByStudentId(name);

        // 업데이트된 사용자 정보가 존재한다면 UserDTO로 변환하여 반환
        if (updatedUser != null) {
            return UserDTO.builder()
                    .name(updatedUser.getName())
                    .phoneNumber(updatedUser.getPhoneNumber())
                    .build();
        }
        else{
            return null;
        }
    };
}
