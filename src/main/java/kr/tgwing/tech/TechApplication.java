package kr.tgwing.tech;

import java.sql.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import kr.tgwing.tech.user.dto.UserDTO;
import kr.tgwing.tech.user.service.UserService;

@SpringBootApplication
@EnableJpaAuditing
public class TechApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechApplication.class, args);
    }

    @Bean
    public CommandLineRunner registerTestUser(UserService userService) {
        return args -> {
            userService.register(UserDTO.builder()
                    .studentId("2018000000")
                    .password("12345678")
                    .email("admin@khu.ac.kr")
                    .name("김철수")
                    .birth(new Date(0))
                    .phoneNumber("01000000000")
                    .role("Admin")
                    .profilePicture("url")
                    .build());
        };
    }
}
