package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.LabelDto;
import org.example.dto.PostDto;
import org.example.dto.mapper.LabelDtoMapper;
import org.example.dto.mapper.LabelMapper;
import org.example.dto.mapper.PostDtoMapper;
import org.example.exception.NotFoundException;
import org.example.repository.impl.LabelRepositoryImpl;
import org.example.repository.impl.PostRepositoryImpl;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class LabelService {

    private final LabelRepositoryImpl labelRepositoryImpl;
    private final PostRepositoryImpl postRepositoryImpl;
    private final LabelMapper labelMapper;
    private final LabelDtoMapper labelDtoMapper;
    private final PostDtoMapper postDtoMapper;

    public List<LabelDto> findAll() {

        return labelRepositoryImpl.findAll().stream()
            .map(labelDtoMapper::map)
            .toList();
    }

    public LabelDto findById(Integer id) {

        if (id == null) {

            return null; //TODO: бросить NotFoundException
        }

        var labelDto = labelDtoMapper.map(labelRepositoryImpl.findById(id));

        //TODO: использовать var
        var posts = postRepositoryImpl.findByLabelId(id).stream()
            .map(postDtoMapper::map)
            .toList();

        labelDto.setPosts(posts);

        return labelDto;
    }

    public LabelDto create(LabelDto newLabelDto) {

        //TODO: данная проверка в методе create не имеет смысла
        if (newLabelDto == null) {

            return null;
        }

        return labelDtoMapper.map(labelRepositoryImpl.create(labelMapper.map(newLabelDto)));
    }

    public LabelDto update(LabelDto labelDto) {

        labelRepositoryImpl.update(labelMapper.map(labelDto));

        log.info("Label with id = {} - edited.", labelDto.getId());

        return labelDto;
    }

    public void deleteById(Integer id) {
        //TODO: бросить созданный в пакете exception NotFoundException
        if (id == null) {

            throw new IllegalArgumentException("id can't be null");
        }

        labelRepositoryImpl.deleteById(id);

        log.info("Label with id = {} - deleted.", id);
    }

    public LabelDto findByName(String name) {

        if (name == null) {
            return null;
        }

        var labelDto = labelDtoMapper.map(labelRepositoryImpl.findByName(name));

        List<PostDto> posts = postRepositoryImpl.findByLabelId(labelDto.getId()).stream()
            .map(postDtoMapper::map)
            .toList();

        labelDto.setPosts(posts);

        return labelDto;
    }

    //TODO: чем этот метод отличается от findByName()?
    // Если я хочу узнать существует ли лейбл по имени, мб я лучше вызову метод findByName и сделаю проверку у себя, зачем дублировать код?
    public boolean isExistByName(String name) {
        if (name == null) {
            return false;
        }

        try {
            return labelRepositoryImpl.findByName(name) != null;
        } catch (NotFoundException e) {
            return false;
        }
    }
}
