package org.example.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.example.domain.Post;
import org.example.dto.PostDto;

@RequiredArgsConstructor
public class PostMapper implements Mapper<PostDto, Post> {

    private final LabelMapper labelMapper;

    @Override
    public Post map(PostDto source) {
        return Post.builder()
            .id(source.getId())
            .content(source.getContent())
            .postStatus(source.getPostStatus())
            .created(source.getCreated())
            .updated(source.getUpdated())
            .writerId(source.getWriterId())
            .labels(source.getLabels().stream()
                .map(labelMapper::map)
                .toList())
            .build();
    }
}
