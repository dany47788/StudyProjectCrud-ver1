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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WriterServiceTest {
    private final LabelDtoMapper labelDtoMapper = new LabelDtoMapper();
    private final LabelMapper labelMapper = new LabelMapper();
    private final PostDtoMapper postDtoMapper = new PostDtoMapper(labelDtoMapper);
    private final PostMapper postMapper = new PostMapper(labelMapper);
    private final WriterDtoMapper writerDtoMapper = new WriterDtoMapper(postDtoMapper);
    private final WriterMapper writerMapper = new WriterMapper(postMapper);
    @Mock
    private PostRepositoryImpl postRepository;
    @Mock
    private WriterRepositoryImpl writerRepository;
    private WriterService writerService;

    @BeforeEach
    void init() {
        writerService = new WriterService(
            writerRepository, postRepository, postDtoMapper, writerDtoMapper, writerMapper);
    }

    @Test
    void testFindAll() {
        given(writerRepository.findAll()).willReturn(new ArrayList<>());

        var result = writerService.findAll();

        assertEquals(new ArrayList<>(), result);
    }

    @Test
    void testIFindById_Found() {
        var expectedResult = new WriterDto(1, "test", "test", new ArrayList<>());
        var expectedPosts = new ArrayList<PostDto>();

        given(writerRepository.findById(10)).willReturn(writerMapper.map(expectedResult));

        expectedPosts.add(new PostDto(1, 1, LocalDateTime.now(), LocalDateTime.now(), "test", PostStatus.ACTIVE, new ArrayList<>()));

        given(postRepository.findByWriterId(10)).willReturn(expectedPosts.stream()
            .map(postMapper::map)
            .toList());

        expectedResult.setPosts(expectedPosts);

        var result = writerService.findById(10);

        Assertions.assertEquals(expectedResult.getId(), result.getId());
        Assertions.assertEquals(expectedResult.getFirstName(), result.getFirstName());
        Assertions.assertEquals(expectedResult.getLastName(), result.getLastName());
        Assertions.assertEquals(expectedResult.getPosts(), result.getPosts());
    }

    @Test
    void testFindById_NotFound() {
        given(writerRepository.findById(any(Integer.class))).willThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> {
            writerService.findById(any(Integer.class));
        });
    }

    @Test
    void testFindById_Null() {
        var result = writerService.findById(null);

        assertNull(result);
    }

    @Test
    void update() {
        var writerDto = new WriterDto(1, "test", "test", new ArrayList<>());
        var extendedResult = writerMapper.map(writerDto);

        given(writerRepository.update(extendedResult)).willReturn(extendedResult);

        var result = writerService.update(writerDto);

        assertEquals(writerDto, result);
    }

    @Test
    void update_null() {
        var result = writerService.update(null);

        assertNull(result);
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> writerService.deleteById(10));
    }

    @Test
    void create() {
        var writerDto = new WriterDto(1, "test", "test", new ArrayList<>());
        var extendedResult = writerMapper.map(writerDto);

        given(writerRepository.create(extendedResult)).willReturn(extendedResult);

        var result = writerService.create(writerDto);

        assertEquals(writerDto, result);
    }

    @Test
    void create_null() {
        var result = writerService.create(null);

        assertNull(result);
    }
}