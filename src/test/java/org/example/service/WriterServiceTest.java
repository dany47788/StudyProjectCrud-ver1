package org.example.service;

import org.example.domain.Writer;
import org.example.domain.enums.PostStatus;
import org.example.dto.PostDto;
import org.example.dto.WriterDto;
import org.example.dto.mapper.*;
import org.example.exception.NotFoundException;
import org.example.repository.impl.PostRepositoryImpl;
import org.example.repository.impl.WriterRepositoryImpl;
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
class WriterServiceTest {
    private LabelDtoMapper labelDtoMapper;
    private LabelMapper labelMapper;
    private PostDtoMapper postDtoMapper;
    private PostMapper postMapper;
    private WriterDtoMapper writerDtoMapper;
    private WriterMapper writerMapper;
    @Mock
    private PostRepositoryImpl postRepository;
    @Mock
    private WriterRepositoryImpl writerRepository;
    private WriterService writerService;

    @BeforeEach
    void init() {
        labelDtoMapper = new LabelDtoMapper();
        labelMapper = new LabelMapper();
        postDtoMapper = new PostDtoMapper(labelDtoMapper);
        postMapper = new PostMapper(labelMapper);
        writerDtoMapper = new WriterDtoMapper(postDtoMapper);
        writerMapper = new WriterMapper(postMapper);

        labelDtoMapper.setPostDtoMapper(postDtoMapper);
        labelMapper.setPostMapper(postMapper);

        writerService = new WriterService(
            writerRepository, postRepository, postDtoMapper, writerDtoMapper, writerMapper);
    }

    @Test
    void testFindAll() {
        var writer = new ArrayList<Writer>();

        for (int i = 1; i < 4; i++) {
            writer.add(new Writer(i, "test" + i, "test" + i));
        }
        given(writerRepository.findAll()).willReturn(writer);

        var result = writerService.findAll();

        assertEquals(writer.stream().map(writerDtoMapper::map).toList(), result);
    }

    @Test
    void testIFindById_Found() {
        var expectedResult = new WriterDto(1, "test", "test");
        var expectedPosts = new ArrayList<PostDto>();

        for (int i = 1; i < 4; i++) {
            expectedPosts.add(new PostDto(i, i, LocalDateTime.now(),
                LocalDateTime.now(), "test" + i, PostStatus.ACTIVE));
        }

        given(writerRepository.findById(any(Integer.class))).willReturn(writerMapper.map(expectedResult));

        given(postRepository.findAllByWriterId(any(Integer.class))).willReturn(expectedPosts.stream()
            .map(postMapper::map)
            .toList());

        expectedResult.setPosts(expectedPosts);

        var result = writerService.findById(any(Integer.class));

        Assertions.assertEquals(expectedResult.getId(), result.getId());
        Assertions.assertEquals(expectedResult.getFirstName(), result.getFirstName());
        Assertions.assertEquals(expectedResult.getLastName(), result.getLastName());
        Assertions.assertEquals(expectedResult.getPosts(), result.getPosts());
    }

    @Test
    void testFindById_NotFound() {
        given(writerRepository.findById(any(Integer.class))).willThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> writerService.findById(any(Integer.class)));
    }

    @Test
    void testFindById_Null() {
        var result = writerService.findById(null);

        assertNull(result);
    }

    @Test
    void update() {
        var inputDto = new WriterDto(1, "test", "test", new ArrayList<>());

        given(writerRepository.update(writerMapper.map(inputDto)))
            .willReturn(writerMapper.map(inputDto));

        var returnedDto = writerService.update(inputDto);

        assertEquals(inputDto, returnedDto);
    }

    @Test
    void update_null() {
        assertThrows(NullPointerException.class, () -> writerService.update(null));
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> writerService.deleteById(any(Integer.class)));
    }

    @Test
    void create() {
        var posts = new ArrayList<PostDto>();

        for (int i = 1; i < 4; i++) {
            posts.add(new PostDto(i, i, LocalDateTime.now(), LocalDateTime.now(), "test" + i, PostStatus.ACTIVE));
        }

        var writerDto = new WriterDto(1, "test", "test");
        writerDto.setPosts(posts);

        var extendedResult = writerMapper.map(writerDto);

        given(writerRepository.create(extendedResult)).willReturn(extendedResult);

        var result = writerService.create(writerDto);

        assertEquals(writerDto, result);
    }

    @Test
    void create_null() {
        assertThrows(NullPointerException.class, () -> writerService.create(null));
    }
}