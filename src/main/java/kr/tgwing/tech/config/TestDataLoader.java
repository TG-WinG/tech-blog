package kr.tgwing.tech.config;

import java.sql.Date;
import java.time.LocalDate;

import kr.tgwing.tech.user.dto.registerdto.UserDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import kr.tgwing.tech.blog.entity.PostEntity;
import kr.tgwing.tech.blog.entity.ReplyEntity;
import kr.tgwing.tech.blog.repository.HashtagRepository;
import kr.tgwing.tech.blog.repository.PostRepository;
import kr.tgwing.tech.blog.repository.ReplyRepository;
import kr.tgwing.tech.user.service.UserService;

@Configuration
public class TestDataLoader {

    @Bean
    @Profile("default")
    public CommandLineRunner loadTestData(UserService userService,
                                          PostRepository postRepository, 
                                          HashtagRepository hashtagRepository, 
                                          ReplyRepository replyRepository) {
        return args -> {
            userService.register(UserDTO.builder()
                .studentNumber("2018000000")
                .phoneNumber("01000000000")
                .email("oldman@khu.ac.kr")
                .name("늙은이")
                .password("12345678")
                .birth(LocalDate.parse("1999-01-01"))
                .build());
            userService.register(UserDTO.builder()
                .studentNumber("2022000000")
                .phoneNumber("01011111111")
                .email("youngman@khu.ac.kr")
                .name("젊은이")
                .password("12345678")
                .birth(LocalDate.parse("2003-01-01"))
                .build());
            postRepository.save(PostEntity.builder()
                .writer(1L)
                .title("sample blog 1")
                .content("sample content 1")
                .thumbnail("sample_thumbail_url_1")
                .build());
            postRepository.save(PostEntity.builder()
                .writer(2L)
                .title("sample blog 2")
                .content("sample content 2")
                .thumbnail("sample_thumbail_url_2")
                .build());
            replyRepository.save(ReplyEntity.builder()
                .writer(2L)
                .post(1L)
                .description("sample comment 1")
                .build());
            replyRepository.save(ReplyEntity.builder()
                .writer(1L)
                .post(1L)
                .description("sample comment 2")
                .build());
        };
    }
}
