package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class Label {
    private final Integer id;
    private final String name;
    private final List<Post> posts;

    public Label(Integer id, String name) {
        this(id, name, new ArrayList<>());
    }
}
