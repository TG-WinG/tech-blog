package kr.tgwing.tech.blog.service;

import kr.tgwing.tech.blog.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    public PostDto getPost(Long postId);
    public PostDto createPost(PostDto requestDto, String token);
    public PostDto updatePost(PostDto postDto, Long userId, Long postId, String token);
    public void deletePost(Long userId, Long postId);
    public Page<PostDto> getPostsInPage(String text, int page, Pageable pageable);
}
