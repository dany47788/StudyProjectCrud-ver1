package org.example.controller;

import org.example.dto.WriterDto;
import org.example.service.WriterService;

import java.util.List;

public class WriterController {
    private final WriterService writerService;

    public WriterController(WriterService writerService) {
        this.writerService = writerService;
    }

    public List<WriterDto> findAll() {
        return writerService.findAll();
    }

    public WriterDto findById(Integer id) {
        return writerService.findById(id);
    }

    public void update(WriterDto writerDto) {
        writerService.update(writerDto);
    }

    public void deleteById(Integer id) {
        writerService.deleteById(id);
    }

    public void create(WriterDto writerDto) {
        writerService.create(writerDto);
    }
}
