package org.example.service;

import org.example.domain.enums.PostStatus;
import org.example.dto.LabelDto;
import org.example.dto.PostDto;
import org.example.dto.mapper.LabelDtoMapper;
import org.example.dto.mapper.LabelMapper;
import org.example.dto.mapper.PostDtoMapper;
import org.example.dto.mapper.PostMapper;
import org.example.exception.NotFoundException;
import org.example.repository.impl.LabelRepositoryImpl;
import org.example.repository.impl.PostRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    private final LabelDtoMapper labelDtoMapper = new LabelDtoMapper();
    private final LabelMapper labelMapper = new LabelMapper();
    private final PostDtoMapper postDtoMapper = new PostDtoMapper(labelDtoMapper);
    private final PostMapper postMapper = new PostMapper(labelMapper);
    @Mock
    private PostRepositoryImpl postRepository;
    @Mock
    private LabelRepositoryImpl labelRepository;
    private PostService postService;

    @BeforeEach
    void init() {
        postService = new PostService(
            postRepository, labelRepository, labelDtoMapper, postDtoMapper, postMapper);
    }

    @Test
    void update() {
        var postDto = new PostDto(1, 1, LocalDateTime.now(), LocalDateTime.now(), "test", PostStatus.ACTIVE);

        var extendedResult = postMapper.map(postDto);

        given(postRepository.update(extendedResult)).willReturn(extendedResult);

        var result = postService.update(postDto);

        assertEquals(postDto, result);
    }

    @Test
    void update_null() {
        var result = postService.update(null);

        assertNull(result);
    }

    @Test
    void create() {
        var postDto = new PostDto(1, 1, LocalDateTime.now(), LocalDateTime.now(), "test", PostStatus.ACTIVE);

        var extendedResult = postMapper.map(postDto);

        given(postRepository.create(extendedResult)).willReturn(extendedResult);

        var result = postService.create(postDto);

        assertEquals(postDto, result);
    }

    @Test
    void create_null() {
        var result = postService.create(null);

        assertNull(result);
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> postService.deleteById(any(Integer.class)));
    }

    @Test
    void testFindAll() {

        given(postRepository.findAll()).willReturn(new ArrayList<>());

        var result = postService.findAll();
        assertEquals(new ArrayList<>(), result);
    }

    @Test
    void testIFindById_Found() {
        var expectedResult = new PostDto(1, 1, LocalDateTime.now(), LocalDateTime.now(), "test", PostStatus.ACTIVE, new ArrayList<>());
        var expectedLabels = new ArrayList<LabelDto>();

        given(postRepository.findById(10)).willReturn(postMapper.map(expectedResult));

        expectedLabels.add(new LabelDto(10, "test", new ArrayList<>()));

        given(labelRepository.findByPostId(10)).willReturn(expectedLabels.stream()
            .map(labelMapper::map)
            .toList());

        expectedResult.setLabels(expectedLabels);

        var result = postService.findById(10);

        Assertions.assertEquals(expectedResult.getId(), result.getId());
        Assertions.assertEquals(expectedResult.getWriterId(), result.getWriterId());
        Assertions.assertEquals(expectedResult.getLabels(), result.getLabels());
        Assertions.assertEquals(expectedResult.getPostStatus(), result.getPostStatus());
        Assertions.assertEquals(expectedResult.getCreated(), result.getCreated());
        Assertions.assertEquals(expectedResult.getUpdated(), result.getUpdated());
        Assertions.assertEquals(expectedResult.getContent(), result.getContent());
    }

    @Test
    void testFindById_NotFound() {
        given(postRepository.findById(any(Integer.class))).willThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> {
            postService.findById(any(Integer.class));
        });
    }

    @Test
    void testFindById_Null() {
        var result = postService.findById(null);

        assertNull(result);
    }
}