package org.example.repository;

import java.util.List;

public interface CrudRepository<T, ID> {

    List<T> findAll();

    T findById(ID id);

    T create(T entity);

    T update(T entity);

    void deleteById(ID id);
}
