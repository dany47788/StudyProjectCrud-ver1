package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.domain.enums.PostStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class Post {
    private final Integer id;
    private final LocalDateTime created;
    private final LocalDateTime updated;
    private final Integer writerId;
    private final String content;
    private final PostStatus postStatus;
    private final List<Label> labels;

    public Post(Integer id, Integer writerId, LocalDateTime created, LocalDateTime updated,
                String content, PostStatus postStatus) {
        this(id, created, updated, writerId, content, postStatus, new ArrayList<>());
    }
}
