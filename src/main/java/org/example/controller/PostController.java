package org.example.controller;

import org.example.dto.PostDto;
import org.example.service.PostService;

import java.util.List;

public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    public void update(PostDto postDto) {
        postService.update(postDto);
    }

    public void create(PostDto postDto) {
        postService.create(postDto);
    }

    public void deleteById(Integer id) {
        postService.deleteById(id);
    }

    public List<PostDto> findAll() {
        return postService.findAll();
    }

    public PostDto findById(Integer id) {
        return postService.findById(id);
    }
}
