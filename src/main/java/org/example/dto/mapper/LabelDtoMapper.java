package org.example.dto.mapper;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.domain.Label;
import org.example.dto.LabelDto;
import org.example.dto.PostDto;

@RequiredArgsConstructor
public class LabelDtoMapper implements Mapper<Label, LabelDto> {
    @Setter
    private PostDtoMapper postDtoMapper;

    @Override
    public LabelDto map(Label source) {
        return LabelDto.builder()
            .id(source.getId())
            .name(source.getName())
            .posts(source.getPosts().stream()
                .map(postDtoMapper::map)
                .toList())
            .build();
    }
}
