package org.example.service;

import org.example.domain.Label;
import org.example.domain.Post;
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
        var inputDto = new PostDto(1, 1, LocalDateTime.now(),
            LocalDateTime.now(), "test", PostStatus.ACTIVE);

        given(postRepository.update(postMapper.map(inputDto)))//check that the service input sent to the repo unchanged
            .willReturn(postMapper.map(inputDto));

        var returnedDto = postService.update(inputDto);

        assertEquals(inputDto, returnedDto);//check that the returned object equal input object
    }

    @Test
    void update_null() {
        assertThrows(NullPointerException.class, () -> postService.update(null));
    }

    @Test
    void create() {
        var labels = new ArrayList<LabelDto>();
        labels.add(new LabelDto(1, "test"));
        labels.add(new LabelDto(2, "test1"));
        labels.add(new LabelDto(3, "test2"));

        var inputDto = PostDto.builder()
            .writerId(1)
            .created(LocalDateTime.now())
            .updated(LocalDateTime.now())
            .content("test")
            .postStatus(PostStatus.ACTIVE)
            .labels(labels)
            .build();

        var extendedResult = PostDto.builder()
            .id(1)
            .writerId(inputDto.getWriterId())
            .created(inputDto.getCreated())
            .updated(inputDto.getUpdated())
            .content(inputDto.getContent())
            .postStatus(inputDto.getPostStatus())
            .labels(labels)
            .build();

        given(postRepository.create(postMapper.map(inputDto))).willReturn(postMapper.map(extendedResult));

        var result = postService.create(inputDto);

        assertEquals(extendedResult.getCreated(), result.getCreated());
        assertEquals(extendedResult.getUpdated(), result.getUpdated());
        assertEquals(extendedResult.getContent(), result.getContent());
        assertEquals(extendedResult.getWriterId(), result.getWriterId());
        assertEquals(extendedResult.getPostStatus(), result.getPostStatus());
        assertEquals(extendedResult.getLabels(), result.getLabels());
        assertNotNull(result.getId());
    }

    @Test
    void create_null() {
        assertThrows(NullPointerException.class, () -> postService.update(null));
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> postService.deleteById(any(Integer.class)));
    }

    @Test
    void delete_null() {
        assertThrows(NullPointerException.class, () -> postService.deleteById(null));
    }

    @Test
    void testFindAll() {
        var posts = new ArrayList<Post>();

        for (int i = 1; i < 4; i++) {
            posts.add(new Post(i, LocalDateTime.now(), LocalDateTime.now(), i, "test"+i, PostStatus.ACTIVE));
        }
        given(postRepository.findAll()).willReturn(posts);

        var result = postService.findAll();

        assertEquals(posts.stream().map(postDtoMapper::map).toList(), result);
    }

    @Test
    void testIFindById_Found() {
        var expectedResult = new PostDto(1, 1, LocalDateTime.now(), LocalDateTime.now(), "test", PostStatus.ACTIVE);
        var expectedLabels = new ArrayList<LabelDto>();

        expectedLabels.add(new LabelDto(10, "test1"));
        expectedLabels.add(new LabelDto(11, "test2"));
        expectedLabels.add(new LabelDto(12, "test3"));

        given(postRepository.findById(any(Integer.class))).willReturn(postMapper.map(expectedResult));

        given(labelRepository.findByPostId(any(Integer.class))).willReturn(expectedLabels.stream()
            .map(labelMapper::map)
            .toList());

        expectedResult.setLabels(expectedLabels);

        var result = postService.findById(any(Integer.class));

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

        assertThrows(NotFoundException.class, () -> postService.findById(any(Integer.class)));
    }

    @Test
    void testFindById_Null() {
        var result = postService.findById(null);

        assertNull(result);
    }
}