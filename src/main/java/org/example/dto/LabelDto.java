package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class LabelDto {
    private Integer id;
    private String name;
    private List<PostDto> posts;

    public LabelDto(Integer id, String name) {
        this(id, name, new ArrayList<>());
    }
}
