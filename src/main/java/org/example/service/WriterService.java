package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.WriterDto;
import org.example.dto.mapper.PostDtoMapper;
import org.example.dto.mapper.WriterDtoMapper;
import org.example.dto.mapper.WriterMapper;
import org.example.repository.impl.PostRepositoryImpl;
import org.example.repository.impl.WriterRepositoryImpl;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class WriterService {

    private final WriterRepositoryImpl writerRepositoryImpl;
    private final PostRepositoryImpl postRepositoryImpl;
    private final PostDtoMapper postDtoMapper;
    private final WriterDtoMapper writerDtoMapper;
    private final WriterMapper writerMapper;

    public List<WriterDto> findAll() {

        return writerRepositoryImpl.findAll().stream()
            .map(writerDtoMapper::map)
            .toList();
    }

    public WriterDto findById(Integer id) {

        if (id == null) {

            return null;
        }

        var writerDto = writerDtoMapper.map(writerRepositoryImpl.findById(id));

        writerDto.setPosts(postRepositoryImpl.findByWriterId(id).stream()
            .map(postDtoMapper::map)
            .toList());

        return writerDto;
    }

    public WriterDto create(WriterDto writerDto) {

        if (writerDto == null) {

            return null;
        }

        writerRepositoryImpl.create(writerMapper.map(writerDto));

        log.info("{} - created.", writerDto);

        return writerDto;
    }

    public WriterDto update(WriterDto writerDto) {

        if (writerDto == null) {

            return null;
        }

        writerRepositoryImpl.update(writerMapper.map(writerDto));

        log.info("{} - updated.", writerDto);

        return writerDto;
    }

    public void deleteById(Integer id) {

        postRepositoryImpl.findByWriterId(id).stream().map(postDtoMapper::map)
            .forEach(e -> postRepositoryImpl.deleteById(e.getId()));

        writerRepositoryImpl.deleteById(id);

        log.info("Writer with id = {} - deleted with all his posts.", id);
    }
}
