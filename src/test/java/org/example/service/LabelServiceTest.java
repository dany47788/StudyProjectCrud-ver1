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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class LabelServiceTest {
    private LabelDtoMapper labelDtoMapper;
    private LabelMapper labelMapper;
    private PostDtoMapper postDtoMapper;
    private PostMapper postMapper;
    @Mock
    private LabelRepositoryImpl labelRepository;
    @Mock
    private PostRepositoryImpl postRepository;
    private LabelService labelService;

    @BeforeEach
    void init() {
        labelDtoMapper = new LabelDtoMapper();
        labelMapper = new LabelMapper();
        postDtoMapper = new PostDtoMapper(labelDtoMapper);
        postMapper = new PostMapper(labelMapper);
        labelService = new LabelService(
            labelRepository, postRepository, labelMapper, labelDtoMapper, postDtoMapper);
    }

    @Test
    void testIFindById_Found() {
        var expectedLabel = new Label(10, "test", new ArrayList<>());
        var expectedPosts = new ArrayList<Post>();

        expectedPosts.add(new Post(
            1, LocalDateTime.now(), LocalDateTime.now(), 1,
            "123", PostStatus.ACTIVE));

        given(labelRepository.findById(any(Integer.class))).willReturn(expectedLabel);

        given(postRepository.findByLabelId(any(Integer.class))).willReturn(expectedPosts);

        var expectedResult = labelDtoMapper.map(expectedLabel);

        expectedResult.setPosts(expectedPosts.stream()
            .map(postDtoMapper::map)
            .toList());

        var returnedDto = labelService.findById(any(Integer.class));

        Assertions.assertEquals(expectedResult.getId(), returnedDto.getId());
        Assertions.assertEquals(expectedResult.getName(), returnedDto.getName());
        Assertions.assertEquals(expectedResult.getPosts(), returnedDto.getPosts());
    }

    @Test
    void testFindById_NotFound() {
        given(labelRepository.findById(any(Integer.class))).willReturn(null);

        assertThrows(NotFoundException.class, () -> labelService.findById(any(Integer.class)));
    }

    @Test
    void testFindById_Null() {
        assertThrows(NotFoundException.class, () -> labelService.findById(null));
    }

    @Test
    void testFindByName_Found() {
        var expectedDto = new LabelDto(10, "test", new ArrayList<>());

        given(labelRepository.findByName("test")).willReturn(labelMapper.map(expectedDto));

        var expectedPosts = new ArrayList<PostDto>();

        expectedPosts.add(new PostDto(
            1, 1, LocalDateTime.now(), LocalDateTime.now(),
            "123", PostStatus.ACTIVE));

        expectedDto.setPosts(expectedPosts);

        given(postRepository.findByLabelId(any(Integer.class)))
            .willReturn(expectedPosts.stream()
            .map(postMapper::map).toList());

        var result = labelService.findByName("test");

        Assertions.assertEquals(expectedDto.getId(), result.getId());
        Assertions.assertEquals(expectedDto.getName(), result.getName());
        Assertions.assertEquals(expectedDto.getPosts(), result.getPosts());
    }

    @Test
    void testFindByName_NotFound() {
        given(labelRepository.findByName("test")).willReturn(null);

        assertThrows(NotFoundException.class, () -> labelService.findByName("test"));
    }

    @Test
    void testFindByName_Null() {
        assertThrows(NotFoundException.class, () -> labelService.findByName(null));
    }

    @Test
    void testFindAll() {
        var labels = new ArrayList<Label>();

        for (int i = 1; i < 4; i++) {
            labels.add(new Label(i, "test"+i));
        }
        given(labelRepository.findAll()).willReturn(labels);

        var result = labelService.findAll();

        assertEquals(labels.stream().map(labelDtoMapper::map).toList(), result);
    }

    @Test
    void create() {
        var inputLabelDto = LabelDto.builder()
            .name("test")
            .build();

        var extendedResult = LabelDto.builder()
            .id(1)
            .name(inputLabelDto.getName())
            .build();

        given(labelRepository.create(labelMapper.map(inputLabelDto)))
            .willReturn(labelMapper.map(extendedResult));

        var result = labelService.create(inputLabelDto);

        assertEquals(extendedResult.getName(), result.getName());
        assertEquals(extendedResult.getPosts(), result.getPosts());
        assertEquals(extendedResult.getId(), result.getId());
        assertNotNull(result.getId());
    }

    @Test
    void create_null() {
        assertThrows(NullPointerException.class, () -> labelService.create(null));
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> labelService.deleteById(any(Integer.class)));
    }

    @Test
    void delete_null() {
        assertThrows(NullPointerException.class, () -> labelService.deleteById(null));
    }

    @Test
    void update() {
        var inputDto = new LabelDto(1, "test");

        given(labelRepository.update(labelMapper.map(inputDto)))
            .willReturn(labelMapper.map(inputDto));

        var returnedDto = labelService.update(inputDto);

        assertEquals(inputDto, returnedDto);
    }

    @Test
    void update_null() {
        assertThrows(NullPointerException.class, () -> labelService.update(null));
    }
}
