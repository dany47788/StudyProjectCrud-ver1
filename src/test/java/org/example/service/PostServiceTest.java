package org.example.service;

import org.example.domain.Post;
import org.example.domain.enums.PostStatus;
import org.example.dto.LabelDto;
import org.example.dto.PostDto;
import org.example.dto.mapper.*;
import org.example.exception.NotFoundException;
import org.example.repository.impl.LabelRepositoryImpl;
import org.example.repository.impl.PostRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
class PostServiceTest {
    private final LabelDtoMapper labelDtoMapper = new LabelDtoMapper();
    private final LabelMapper labelMapper = new LabelMapper();
    private final PostDtoMapper postDtoMapper = new PostDtoMapper(labelDtoMapper);
    private final PostMapper postMapper = new PostMapper(labelMapper);
    private final WriterDtoMapper writerDtoMapper = new WriterDtoMapper(postDtoMapper);
    private final WriterMapper writerMapper = new WriterMapper(postMapper);
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

    @Nested
    public class TestUpdate {
        @Test
        void update() {

            var postDto = new PostDto(1, 1, LocalDateTime.now(),LocalDateTime.now(), "test", PostStatus.ACTIVE, new ArrayList<>());

            Post extendedResult = postMapper.map(postDto);

            given(postRepository.update(extendedResult)).willReturn(extendedResult);

            PostDto result = postService.update(postDto);

            assertEquals(postDto, result);
        }

        @Test
        void update_null() {

            PostDto result = postService.update(null);

            assertNull(result);
        }
    }

    @Nested
    public class TestCreate {
        @Test
        void create() {

            var postDto = new PostDto(1, 1, LocalDateTime.now(),LocalDateTime.now(), "test", PostStatus.ACTIVE, new ArrayList<>());

            Post extendedResult = postMapper.map(postDto);

            given(postRepository.create(extendedResult)).willReturn(extendedResult);

            PostDto result = postService.create(postDto);

            assertEquals(postDto, result);
        }

        @Test
        void create_null() {

            PostDto result = postService.create(null);

            assertNull(result);
        }
    }

    @Nested
    public class TestDeleteById {
        @Test
        void delete() {

            assertDoesNotThrow(() -> {
                postService.deleteById(10);
            });
        }
    }

    @Nested
    public class TestFindAll {
        @Test
        void testFindAll() {

            given(postRepository.findAll()).willReturn(new ArrayList<>());

            List<PostDto> result = postService.findAll();
            assertEquals(new ArrayList<>(), result);
        }
    }

    @Nested
    public class TestFindById {
        @Test
        void testIFindById_Found() {

            var expectedResult = new PostDto(1, 1, LocalDateTime.now(),LocalDateTime.now(), "test", PostStatus.ACTIVE, new ArrayList<>());

            given(postRepository.findById(10)).willReturn(postMapper.map(expectedResult));

            List<LabelDto> expectedLabels = new ArrayList<>();
            expectedLabels.add(new LabelDto(10,"test",new ArrayList<>()));

            given(labelRepository.findByPostId(10)).willReturn(expectedLabels.stream()
                .map(labelMapper::map)
                .toList());

            expectedResult.setLabels(expectedLabels);

            PostDto result = postService.findById(10);

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

            PostDto result = postService.findById(null);

            assertNull(result);
        }
    }
}