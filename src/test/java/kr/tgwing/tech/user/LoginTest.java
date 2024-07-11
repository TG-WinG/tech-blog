package kr.tgwing.tech.user;

import kr.tgwing.tech.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoginTest {

    @Autowired
    UserRepository userRepository;

//    @Test
//    @DisplayName("회원객체의 Date형식이 정확히 저장되는지를 확인하는 테스트")
//    public void registerTest() {
//
//        //given
//        UserEntity userEntity = new UserEntity();
//        userEntity.setId(5L);
//        userEntity.setStudentId("2020105622");
//        userEntity.setPassword("1234");
//        userEntity.setName("송성훈");
//        userEntity.setEmail("sonng@gmail.com");
//        userEntity.setBirth(Date.valueOf("2001-09-14"));
//        userEntity.setPhoneNumber("010-6348-5309");
//        userEntity.setRole("ROLE_USER");
//
//        //when
//        UserEntity result = userRepository.save(userEntity);
//
//        //then
//        Assertions.assertThat(userEntity.getBirth().getClass()).isEqualTo(result.getBirth().getClass());
//    }


}
