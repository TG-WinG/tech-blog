package kr.tgwing.tech.config;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.RequiredArgsConstructor;

import kr.tgwing.tech.blog.entity.Comment;
import kr.tgwing.tech.blog.entity.Hashtag;
import kr.tgwing.tech.blog.entity.Post;
import kr.tgwing.tech.blog.entity.Reply;
import kr.tgwing.tech.blog.repository.CommentRepository;
import kr.tgwing.tech.blog.repository.PostRepository;
import kr.tgwing.tech.blog.repository.ReplyRepository;
import kr.tgwing.tech.project.domain.Image;
import kr.tgwing.tech.project.domain.Link;
import kr.tgwing.tech.project.domain.Participant;
import kr.tgwing.tech.project.domain.Project;
import kr.tgwing.tech.project.domain.Enum.Part;
import kr.tgwing.tech.project.repository.LinkRepository;
import kr.tgwing.tech.project.repository.ParticipantRepository;
import kr.tgwing.tech.project.repository.ProjectRepository;
import kr.tgwing.tech.user.entity.User;
import kr.tgwing.tech.user.repository.UserRepository;

@Configuration
@RequiredArgsConstructor
public class TestDataLoader {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    @Profile({"dev", "default"})
    public CommandLineRunner loadTestData(PostRepository postRepository,
                                          UserRepository userRepository,
                                          CommentRepository commentRepository,
                                          ReplyRepository replyRepository,
                                          ProjectRepository projectRepository,
                                          ParticipantRepository participantRepository,
                                          LinkRepository linkRepository
    ) {
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
            Reply reply1 = Reply.builder()
                    .content("sample reply 1")
                    .post(post1)
                    .comment(comment1)
                    .writer(writer1)
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
                post.increaseCommentCount();

                postRepository.save(post);
                commentRepository.save(comment);
            }

            post1.getHashtags().add(tag1);
            post1.getHashtags().add(tag3);
            post2.getHashtags().add(tag2);

            post1.getComments().add(comment1);
            post1.increaseCommentCount();
            comment1.getReplies().add(reply1);
            post1.increaseCommentCount();

            postRepository.save(post1);
            postRepository.save(post2);
            commentRepository.save(comment1);
            replyRepository.save(reply1);

            Project project = Project.builder()
                    .title("프로젝트")
                    .description("아오 " +
                            "js아오 ㄹ먼에ㅐ래")
                    .start(LocalDate.parse("2024-05-01"))
                    .end(LocalDate.parse("2024-10-31"))
                    .devStatus("진행중")
                    .devType("웹")
                    .imageUrls(new ArrayList<>())
                    .links(new ArrayList<>())
                    .participants(new ArrayList<>())
                    .build();


            Image image1 = Image.builder()
                    .imageUrl("img1")
                    .project(project)
                    .build();
            Image image2 = Image.builder()
                    .imageUrl("img2")
                    .project(project)
                    .build();
            Image image3 = Image.builder()
                    .imageUrl("img3")
                    .project(project)
                    .build();

            Link github = Link.builder()
                    .description("github")
                    .url("singsangssong.github.com")
                    .project(project)
                    .build();
            Link notion = Link.builder()
                    .description("notion")
                    .url("notion.no")
                    .project(project)
                    .build();

            Participant participant1 = Participant.builder()
                    .part(Part.DESIGNER)
                    .studentNumber("2020211121")
                    .name("design")
                    .major("디자인과")
                    .project(project)
                    .build();
            Participant participant2 = Participant.builder()
                    .part(Part.BACK)
                    .studentNumber("2018000000")
                    .name("늙은이")
                    .major("컴공")
                    .project(project)
                    .user(writer1)
                    .build();
            Participant participant3 = Participant.builder()
                    .part(Part.FRONT)
                    .studentNumber("2022000000")
                    .name("젊은이")
                    .project(project)
                    .major("컴공")
                    .user(writer2)
                    .build();

            project.getParticipants().add(participant1);
            project.getParticipants().add(participant2);
            project.getParticipants().add(participant3);

            project.getLinks().add(github);
            project.getLinks().add(notion);

            project.getImageUrls().add(image1);
            project.getImageUrls().add(image2);
            project.getImageUrls().add(image3);

            projectRepository.save(project);

        };
    }
}

