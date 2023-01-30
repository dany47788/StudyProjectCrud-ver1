package org.example.service;

import org.example.dto.LabelDto;
import org.example.dto.PostDto;
import org.example.domain.Label;
import org.example.domain.Post;
import org.example.domain.enums.PostStatus;
import org.example.dto.mapper.LabelDtoMapper;
import org.example.dto.mapper.LabelMapper;
import org.example.dto.mapper.PostDtoMapper;
import org.example.dto.mapper.PostMapper;
import org.example.exception.NotFoundException;
import org.example.repository.impl.LabelRepositoryImpl;
import org.example.repository.impl.PostRepositoryImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
public class LabelServiceTest {
    private final LabelDtoMapper labelDtoMapper = new LabelDtoMapper();
    private final LabelMapper labelMapper = new LabelMapper();
    private final PostDtoMapper postDtoMapper = new PostDtoMapper(labelDtoMapper);
    private final PostMapper postMapper = new PostMapper(labelMapper);
    @Mock
    private LabelRepositoryImpl labelRepository;
    @Mock
    private PostRepositoryImpl postRepository;
    private LabelService labelService;

    @BeforeEach
    void init() {
        //TODO: где пробелы между запятыми?
        // все создания объектов можно перенести в метод beforeEach
        labelService = new LabelService(
            labelRepository, postRepository, labelMapper, labelDtoMapper, postDtoMapper);
    }

    //TODO: делать обычными методами
    @Nested
    public class TestIsExist {

        @Test
        void testIsExistTrue() {

            Mockito.doReturn(
                new Label(15, "exitedLabelName", new ArrayList<>()))
                .when(labelRepository)
                .findByName("exitedLabelName");

            boolean result = labelService.isExistByName("exitedLabelName");

            assertTrue(result);
        }

        @Test
        void testIsExistFalse() {
            given(labelRepository.findByName("notExistedName")).willThrow(NotFoundException.class);

            boolean result = labelService.isExistByName("notExistedName");
            //TODO: есть специальный assertThrows()
            assertFalse(result);
        }

        @Test
        void testIsExistNull() {

            given((labelService.isExistByName(null))).willReturn(null);

            boolean result = labelService.isExistByName(null);

            assertFalse(result);
        }
    }

    @Nested
    public class TestFindById {

        @Test
        void testIFindById_Found() {

            var expectedLabel = new Label(10,"test",new ArrayList<>());
            //TODO: используем var
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

            given(labelRepository.findById(any(Integer.class))).willThrow(NotFoundException.class);

            assertThrows(NotFoundException.class, () -> {
                labelService.findById(10);
            });
        }

        @Test
        void testFindById_Null() {

            LabelDto result = labelService.findById(null);

            assertNull(result);
        }
    }

    @Nested
    public class TestFindByName {
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

            given(labelRepository.findByName("test")).willThrow(NotFoundException.class);

            //TODO: фигурные скобки опускаются когда один вызов функции (replace with expression lambda)
            assertThrows(NotFoundException.class, () -> {
                labelService.findByName("test");
            });
        }

        @Test
        void testFindByName_Null() {

            LabelDto result = labelService.findByName(null);

            assertNull(result);
        }
    }

    @Nested
    public class TestFindAll {

        @Test
        void testFindAll() {

            //TODO: почему стабишь пустой список? надо стабить заполненный список ~ 2-3 элемента, иначе какой смысл в этом тесте?
            given(labelRepository.findAll()).willReturn(new ArrayList<>());

            List<LabelDto> result = labelService.findAll();
            assertEquals(new ArrayList<>(), result);
        }
    }

    @Nested
    public class TestCreate {
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
    }

    @Nested
    public class TestDelete {
        @Test
        void delete() {

            assertDoesNotThrow(() -> {
                labelService.deleteById(10);
            });
        }
    }

    @Nested
    public class TestUpdate {
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

            LabelDto result = labelService.update(null);

            assertNull(result);
        }
    }
}
