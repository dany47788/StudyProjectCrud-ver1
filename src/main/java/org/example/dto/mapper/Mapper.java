package org.example.dto.mapper;

public interface Mapper<S, T> {

    T map(S source);
}
