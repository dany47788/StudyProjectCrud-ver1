package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.PostDto;
import org.example.service.PostService;

import java.util.List;

@AllArgsConstructor
public class PostController {

    private final PostService postService;

    public List<PostDto> findAll() {
        return postService.findAll();
    }

    public PostDto findById(Integer id) {
        return postService.findById(id);
    }

    public void create(PostDto postDto) {
        postService.create(postDto);
    }

    public void update(PostDto postDto) {
        postService.update(postDto);
    }

    public void deleteById(Integer id) {
        postService.deleteById(id);
    }
}
