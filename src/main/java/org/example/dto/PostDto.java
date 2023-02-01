package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.domain.enums.PostStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class PostDto {
    private Integer id;
    private Integer writerId;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String content;
    private PostStatus postStatus;
    private List<LabelDto> labels;

    public PostDto(Integer id, Integer writerId, LocalDateTime created,
                   LocalDateTime updated, String content, PostStatus postStatus) {
        this(id, writerId, created, updated, content, postStatus, new ArrayList<>());
    }
}
