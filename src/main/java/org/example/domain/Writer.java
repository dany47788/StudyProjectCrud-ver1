package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class Writer {
    private final Integer id;
    private final String firstName;
    private final String lastName;
    private final List<Post> posts;

    public Writer(Integer id, String firstName, String lastName) {
        this(id, firstName, lastName, new ArrayList<>());
    }
}
