package kr.tgwing.tech.config;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import kr.tgwing.tech.blog.entity.Comment;
import kr.tgwing.tech.blog.entity.Hashtag;
import kr.tgwing.tech.blog.entity.Post;
import kr.tgwing.tech.blog.repository.PostRepository;
import kr.tgwing.tech.user.entity.User;
import kr.tgwing.tech.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class TestDataLoader {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    @Profile("default")
    public CommandLineRunner loadTestData(PostRepository postRepository,
                                          UserRepository userRepository) {
        return args -> {
            User writer1 = User.builder()
                    .studentNumber("2018000000")
                    .phoneNumber("01000000000")
                    .email("oldman@khu.ac.kr")
                    .name("늙은이")
                    .password("12345678")
                    .birth(LocalDate.parse("1999-01-01"))
                    .build();
            User writer2 = User.builder()
                    .studentNumber("2022000000")
                    .phoneNumber("01011111111")
                    .email("youngman@khu.ac.kr")
                    .name("젊은이")
                    .password("12345678")
                    .birth(LocalDate.parse("2003-01-01"))
                    .build();
            writer1.hashPassword(bCryptPasswordEncoder);
            writer2.hashPassword(bCryptPasswordEncoder);

            userRepository.save(writer1);
            userRepository.save(writer2);

            Post post1 = Post.builder()
                    .title("sample blog 1")
                    .content("sample content 1")
                    .thumbnail("sample thumbnail 1")
                    .writer(writer1)
                    .build();
            Post post2 = Post.builder()
                    .title("sample blog 2")
                    .content("sample content 2")
                    .thumbnail("sample thumbnail 2")
                    .writer(writer2)
                    .build();
            Hashtag tag1 = Hashtag.builder()
                    .name("tag1")
                    .post(post1)
                    .build();
            Hashtag tag2 = Hashtag.builder()
                    .name("tag2")
                    .post(post2)
                    .build();
            Hashtag tag3 = Hashtag.builder()
                    .name("tag2")
                    .post(post1)
                    .build();
            Comment comment1 = Comment.builder()
                    .content("sample comment 1")
                    .post(post1)
                    .writer(writer2)
                    .build();

            for(int i=4; i<50; i++) {
                Post post = Post.builder()
                        .title("sample blog" + i)
                        .content("sample content" + i)
                        .thumbnail("sample thumbnail" + i)
                        .writer(writer1)
                        .build();

                Hashtag tag = Hashtag.builder()
                        .name("tag" + i)
                        .post(post)
                        .build();

                Comment comment = Comment.builder()
                        .content("sample comment" + i)
                        .post(post)
                        .writer(writer2)
                        .build();

                post.getComments().add(comment);
                post.getHashtags().add(tag);

                postRepository.save(post);
            }

            post1.getHashtags().add(tag1);
            post1.getHashtags().add(tag3);
            post2.getHashtags().add(tag2);

            post1.getComments().add(comment1);

            postRepository.save(post1);
            postRepository.save(post2);
        };
    }
}

