package org.example.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.domain.enums.PostStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public Post(Integer id, LocalDateTime created, LocalDateTime updated,
                Integer writerId, String content, PostStatus postStatus) {
        this(id,created, updated, writerId, content, postStatus, new ArrayList<>());
    }

    public Post(Integer id, LocalDateTime created, LocalDateTime updated, Integer writerId,
                String content, PostStatus postStatus, List<Label> labels) {
        this.id = id;
        this.created = created;
        this.updated = updated;
        this.writerId = writerId;
        this.content = content;
        this.postStatus = postStatus;
        this.labels = labels;
    }
}
