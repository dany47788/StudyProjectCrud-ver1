package org.example.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.example.domain.Post;
import org.example.dto.PostDto;

@RequiredArgsConstructor
public class PostDtoMapper implements Mapper<Post, PostDto> {
    private final LabelDtoMapper labelDtoMapper;

    @Override
    public PostDto map(Post source) {
        return PostDto.builder()
            .id(source.getId())
            .content(source.getContent())
            .postStatus(source.getPostStatus())
            .created(source.getCreated())
            .updated(source.getUpdated())
            .writerId(source.getWriterId())
            .labels(source.getLabels()
                .stream()
                .map(labelDtoMapper::map)
                .toList())
            .build();
    }
}
