package kr.tgwing.tech.blog;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import kr.tgwing.tech.annotation.IntegrationTest;
import kr.tgwing.tech.blog.entity.Comment;
import kr.tgwing.tech.blog.entity.Hashtag;
import kr.tgwing.tech.blog.entity.Post;
import kr.tgwing.tech.blog.entity.Reply;
import kr.tgwing.tech.blog.repository.CommentRepository;
import kr.tgwing.tech.blog.repository.PostRepository;
import kr.tgwing.tech.blog.repository.ReplyRepository;
import kr.tgwing.tech.user.entity.User;
import kr.tgwing.tech.user.repository.UserRepository;

/**
 * PostIntegrationTest
 */
@IntegrationTest
public class BlogIntegrationTest {

    @Autowired MockMvc mvc;

    @BeforeAll
    static void prepare_test_data(
        @Autowired BCryptPasswordEncoder bCryptPasswordEncoder,
        @Autowired UserRepository userRepository,
        @Autowired PostRepository postRepository,
        @Autowired CommentRepository commentRepository,
        @Autowired ReplyRepository replyRepository
    ) {
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
                .name("tag3")
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
    }

    @Test
    void get_first_page_of_posts() throws Exception {
        mvc.perform(get("/post"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void get_a_post_by_id() throws Exception {
        mvc.perform(get("/post/{postId}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("sample blog 1"))
            .andExpect(jsonPath("$.content").value("sample content 1"))
            .andExpect(jsonPath("$.thumbnail").value("sample thumbnail 1"))
            .andExpect(jsonPath("$.likeCount").value(0))
            .andExpect(jsonPath("$.commentCount").value(2));
    }

    @Test
    void get_posts_title_contain_keyword() throws Exception {
        mvc.perform(get("/post?keyword=1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void get_posts_has_hashtag() throws Exception {
        mvc.perform(get("/post?hashtag=tag1,tag2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void get_my_posts() throws Exception {
        performAsUser(get("/post?me=true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void throw_when_get_my_posts_but_not_logged_in() throws Exception {
        mvc.perform(get("/post?me=true"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void get_list_of_all_hashtags() throws Exception {
        mvc.perform(get("/hashtag"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements").value(3));
    }

    @Test
    void get_hashtags_containing_keword() throws Exception {
        final String keyword = "1";
        mvc.perform(get("/hashtag").queryParam("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void get_hashtags_of_post_that_i_wrote() throws Exception {
        performAsUser(get("/hashtag").queryParam("me", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements").value(2));
    }

    private ResultActions performAsUser(MockHttpServletRequestBuilder builder) throws Exception {
        var result = mvc.perform(post("/login")
            .param("username", "2018000000")
            .param("password", "12345678"))
            .andExpect(status().isOk())
            .andExpect(header().exists("Authorization"))
            .andReturn();

        String token = result.getResponse().getHeader("Authorization");
        return mvc.perform(builder.header("Authorization", token));
    }

}
