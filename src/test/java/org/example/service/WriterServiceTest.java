package org.example.service;

import org.example.domain.Post;
import org.example.domain.Writer;
import org.example.domain.enums.PostStatus;
import org.example.dto.LabelDto;
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

        expectedPosts.add(new PostDto(1, 1, LocalDateTime.now(), LocalDateTime.now(), "test1", PostStatus.ACTIVE));
        expectedPosts.add(new PostDto(2, 2, LocalDateTime.now(), LocalDateTime.now(), "test2", PostStatus.ACTIVE));
        expectedPosts.add(new PostDto(3, 3, LocalDateTime.now(), LocalDateTime.now(), "test3", PostStatus.ACTIVE));

        given(writerRepository.findById(any(Integer.class))).willReturn(writerMapper.map(expectedResult));

        given(postRepository.findByWriterId(any(Integer.class))).willReturn(expectedPosts.stream()
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
        var result = writerService.update(null);

        assertNull(result);
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> writerService.deleteById(any(Integer.class)));
    }

    @Test
    void delete_null() {
        assertThrows(NullPointerException.class, () -> writerService.deleteById(null));
    }

    @Test
    void create() {
        var posts = new ArrayList<Post>();

        posts.add(new Post(1, LocalDateTime.now(), LocalDateTime.now(), 1, "test1", PostStatus.ACTIVE));
        posts.add(new Post(2, LocalDateTime.now(), LocalDateTime.now(), 2, "test2", PostStatus.ACTIVE));
        posts.add(new Post(3, LocalDateTime.now(), LocalDateTime.now(), 3, "test3", PostStatus.ACTIVE));

        var writerDto = new WriterDto(1, "test", "test", new ArrayList<>());
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