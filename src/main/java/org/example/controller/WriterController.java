package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.WriterDto;
import org.example.service.WriterService;

import java.util.List;

@AllArgsConstructor
public class WriterController {

    private final WriterService writerService;

    public List<WriterDto> findAll() {
        return writerService.findAll();
    }

    public WriterDto findById(Integer id) {
        return writerService.findById(id);
    }

    public void create(WriterDto writerDto) {
        writerService.create(writerDto);
    }

    public void update(WriterDto writerDto) {
        writerService.update(writerDto);
    }

    public void deleteById(Integer id) {
        writerService.deleteById(id);
    }
}
