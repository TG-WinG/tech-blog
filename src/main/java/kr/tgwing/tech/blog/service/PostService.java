package kr.tgwing.tech.blog.service;

import kr.tgwing.tech.blog.dto.PostCreationDto;
import kr.tgwing.tech.blog.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    public PostDto getPost(Long postId);
    public PostDto createPost(PostCreationDto requestDto, String token);
    public PostDto updatePost(PostDto postDto, Long postId, String utilStudentId);
    public void deletePost(Long postId, String utilStudentId);
    public Page<PostDto> getPostsInPage(String text, Pageable pageable);
}
