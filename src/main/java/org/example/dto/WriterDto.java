package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WriterDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private List<PostDto> posts;
}
