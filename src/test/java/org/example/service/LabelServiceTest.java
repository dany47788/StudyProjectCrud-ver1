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
        List<Post> expectedPosts = new ArrayList<>();

        expectedPosts.add(new Post(
            1, LocalDateTime.now(), LocalDateTime.now(), 1,
            "123", PostStatus.ACTIVE, new ArrayList<>()));

        given(labelRepository.findById(10)).willReturn(expectedLabel);

        given(postRepository.findByLabelId(10)).willReturn(expectedPosts);

        LabelDto expectedResult = labelDtoMapper.map(expectedLabel);
        expectedResult.setPosts(expectedPosts.stream()
            .map(postDtoMapper::map)
            .toList());

        LabelDto returnedDto = labelService.findById(10);

        Assertions.assertEquals(expectedResult.getId(), returnedDto.getId());
        Assertions.assertEquals(expectedResult.getName(), returnedDto.getName());
        Assertions.assertEquals(expectedResult.getPosts(), returnedDto.getPosts());
    }

    @Test
    void testFindById_NotFound() {

        given(labelRepository.findById(any(Integer.class))).willReturn(null);

        assertThrows(NotFoundException.class, () -> {
            labelService.findById(10);
        });
    }

    @Test
    void testFindById_Null() {

        LabelDto result = labelService.findById(null);

        assertNull(result);
    }

    @Test
    void testFindByName_Found() {
        var expectedDto = new LabelDto(10, "test", new ArrayList<>());

        given(labelRepository.findByName("test")).willReturn(labelMapper.map(expectedDto));

        List<PostDto> expectedPosts = new ArrayList<>();
        expectedPosts.add(new PostDto(
            1, 1, LocalDateTime.now(), LocalDateTime.now(),
            "123", PostStatus.ACTIVE, new ArrayList<>()));

        expectedDto.setPosts(expectedPosts);

        given(postRepository.findByLabelId(any(Integer.class))).willReturn(expectedPosts.stream()
            .map(postMapper::map).toList());

        LabelDto result = labelService.findByName("test");

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

        LabelDto result = labelService.findByName(null);

        assertNull(result);
    }

    @Test
    void testFindAll() {

        //TODO: почему стабишь пустой список? надо стабить заполненный список ~ 2-3 элемента, иначе какой смысл в этом тесте?
        given(labelRepository.findAll()).willReturn(new ArrayList<>());

        List<LabelDto> result = labelService.findAll();
        assertEquals(new ArrayList<>(), result);
    }

    @Test
    void create() {

        var labelDto = new LabelDto(1, "test", new ArrayList<>());

        Label extendedResult = labelMapper.map(labelDto);

        //TODO: вместо labelRepository.create(extendedResult)
        // используется labelRepository.create(any())
        // а иначе ты создаешь expected и возвращаешь expected 😀
        // и второе, создаешь ты лэйбл без id, а получаешь уже с id, это наверное нужно проверить?
        given(labelRepository.create(extendedResult)).willReturn(extendedResult);

        LabelDto result = labelService.create(labelDto);

        assertEquals(labelDto, result);
    }

    @Test
    void create_null() {

        LabelDto result = labelService.create(null);

        assertNull(result);
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> {
            labelService.deleteById(any(Integer.class));
        });
    }

    @Test
    void update() {

        var labelDto = new LabelDto(1, "test", new ArrayList<>());

        Label extendedResult = labelMapper.map(labelDto);

        given(labelRepository.update(extendedResult)).willReturn(extendedResult);

        LabelDto result = labelService.update(labelDto);

        assertEquals(labelDto, result);
    }

    @Test
    void update_null() {
        assertThrows(NullPointerException.class, () -> labelService.update(null));
    }
}
