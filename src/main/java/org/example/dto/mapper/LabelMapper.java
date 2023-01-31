package org.example.dto.mapper;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.domain.Label;
import org.example.dto.LabelDto;

@RequiredArgsConstructor
public class LabelMapper implements Mapper<LabelDto, Label> {
    @Setter
    private PostMapper postMapper;

    @Override
    public Label map(LabelDto source) {
        return Label.builder()
            .id(source.getId())
            .name(source.getName())
            .posts(source.getPosts().stream()
                .map(postMapper::map)
                .toList())
            .build();
    }
}
