package org.example.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.example.domain.Writer;
import org.example.dto.WriterDto;

@RequiredArgsConstructor
public class WriterDtoMapper implements Mapper<Writer, WriterDto> {

    private final PostDtoMapper postDtoMapper;

    @Override
    public WriterDto map(Writer source) {
        return WriterDto.builder()
            .id(source.getId())
            .firstName(source.getFirstName())
            .lastName(source.getLastName())
            .posts(source.getPosts()
                .stream()
                .map(postDtoMapper::map)
                .toList())
            .build();
    }
}
