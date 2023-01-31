package org.example;

import org.example.domain.Writer;
import org.example.repository.impl.WriterRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        WriterRepositoryImpl writerRepository = new WriterRepositoryImpl();
        writerRepository.deleteById(null);
    }
}