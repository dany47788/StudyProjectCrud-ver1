package org.example.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.example.domain.Writer;
import org.example.dto.WriterDto;

@RequiredArgsConstructor
public class WriterMapper implements Mapper<WriterDto, Writer> {

    private final PostMapper postMapper;

    @Override
    public Writer map(WriterDto source) {
        return Writer.builder()
            .id(source.getId())
            .firstName(source.getFirstName())
            .lastName(source.getLastName())
            .posts(source.getPosts().stream()
                .map(postMapper::map)
                .toList())
            .build();
    }
}
