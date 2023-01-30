package org.example.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.example.domain.Label;
import org.example.dto.LabelDto;

@RequiredArgsConstructor
public class LabelDtoMapper implements Mapper<Label, LabelDto> {
    @Override
    public LabelDto map(Label source) {
        return LabelDto.builder()
            .id(source.getId())
            .name(source.getName())
            .posts(source.getPosts())
            .build();
    }
}
