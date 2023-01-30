package org.example;

import org.example.domain.Writer;
import org.example.repository.impl.WriterRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        WriterRepositoryImpl writerRepository = new WriterRepositoryImpl();
        Writer writer = writerRepository.findById(28);
        System.out.println(writer);
    }
}