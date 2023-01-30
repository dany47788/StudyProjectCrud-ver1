package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.enums.PostStatus;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
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
}
