package org.example.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    public Writer(Integer id, String firstName, String lastName, List<Post> posts) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.posts = posts;
    }
}
