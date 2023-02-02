package org.example.repository;

import org.example.domain.Label;

import java.util.List;

public interface LabelRepository extends CrudRepository<Label, Integer> {
    Label findByName(String name);

    List<Label> findAllByPostId(Integer postId);
}
