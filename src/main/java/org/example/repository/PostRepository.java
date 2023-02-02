package org.example.repository;

import org.example.domain.Post;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {
    List<Post> findAllByLabelId(Integer labelId);

    List<Post> findAllByWriterId(Integer writerId);

}


