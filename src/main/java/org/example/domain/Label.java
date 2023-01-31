package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.dto.PostDto;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class Label {
    private final Integer id;
    private final String name;
    private final List<PostDto> posts;

    public Label(Integer id, String name) {
        this(id, name, new ArrayList<>());
    }
}
