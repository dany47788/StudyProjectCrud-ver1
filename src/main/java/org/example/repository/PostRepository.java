package org.example.repository;

import org.example.domain.Post;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {
    List<Post> findByLabelId(Integer labelId);

    List<Post> findByWriterId(Integer writerId);

}


