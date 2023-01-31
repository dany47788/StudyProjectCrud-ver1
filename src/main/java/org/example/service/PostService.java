package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Post;
import org.example.dto.PostDto;
import org.example.dto.mapper.LabelDtoMapper;
import org.example.dto.mapper.PostDtoMapper;
import org.example.dto.mapper.PostMapper;
import org.example.exception.NotFoundException;
import org.example.model.AppStatusCode;
import org.example.repository.impl.LabelRepositoryImpl;
import org.example.repository.impl.PostRepositoryImpl;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepositoryImpl postRepositoryImpl;
    private final LabelRepositoryImpl labelRepositoryImpl;
    private final LabelDtoMapper labelDtoMapper;
    private final PostDtoMapper postDtoMapper;
    private final PostMapper postMapper;

    public List<PostDto> findAll() {
        return postRepositoryImpl.findAll().stream()
            .map(postDtoMapper::map)
            .toList();
    }

    public PostDto findById(Integer id) {
        if (id == null) {
            return null;
        }
        try {
            var postDto = postDtoMapper.map(postRepositoryImpl.findById(id));

            postDto.setLabels(labelRepositoryImpl.findByPostId(id).stream()
                .map(labelDtoMapper::map)
                .toList());

            return postDto;
        } catch (NullPointerException e) {
            throw new NotFoundException(AppStatusCode.NOT_FOUND_EXCEPTION);
        }
    }

    public PostDto create(PostDto postDto) {
        return postDtoMapper.map(postRepositoryImpl.create(postMapper.map(postDto)));
    }

    public PostDto update(PostDto postDto) {
        postRepositoryImpl.update(postMapper.map(postDto));

        log.info("Post with id = {} - updated.", postDto.getId());

        return postDto;
    }

    public void deleteById(Integer id) {
        postRepositoryImpl.deleteById(id);

        log.info("Post with id = {} - deleted.", id);
    }
}
