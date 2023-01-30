package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.LabelDto;
import org.example.dto.mapper.LabelDtoMapper;
import org.example.dto.mapper.LabelMapper;
import org.example.dto.mapper.PostDtoMapper;
import org.example.exception.NotFoundException;
import org.example.model.AppStatusCode;
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
            throw new NotFoundException(AppStatusCode.IS_NULL_EXCEPTION);
        }
        try {
            var labelDto = labelDtoMapper.map(labelRepositoryImpl.findById(id));

            var posts = postRepositoryImpl.findByLabelId(id).stream()
                .map(postDtoMapper::map)
                .toList();

            labelDto.setPosts(posts);

            return labelDto;
        } catch (NullPointerException e) {
            throw new NotFoundException(AppStatusCode.NOT_FOUND_EXCEPTION);
        }
    }

    public LabelDto create(LabelDto newLabelDto) {
        return labelDtoMapper.map(labelRepositoryImpl.create(labelMapper.map(newLabelDto)));
    }

    public LabelDto update(LabelDto labelDto) {
        labelRepositoryImpl.update(labelMapper.map(labelDto));

        log.info("Label with id = {} - edited.", labelDto.getId());

        return labelDto;
    }

    public void deleteById(Integer id) {
        labelRepositoryImpl.deleteById(id);

        log.info("Label with id = {} - deleted.", id);
    }

    public LabelDto findByName(String name) {
        if (name == null) {
            throw new NotFoundException(AppStatusCode.IS_NULL_EXCEPTION);
        }
        try {
            var labelDto = labelDtoMapper.map(labelRepositoryImpl.findByName(name));

            var posts = postRepositoryImpl.findByLabelId(labelDto.getId()).stream()
                .map(postDtoMapper::map)
                .toList();

            labelDto.setPosts(posts);

            return labelDto;
        } catch (NullPointerException e) {
            throw new NotFoundException(AppStatusCode.NOT_FOUND_EXCEPTION);
        }
    }
}
