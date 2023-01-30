package org.example.controller;

import org.example.dto.LabelDto;
import org.example.service.LabelService;

import java.util.List;

public class LabelController {
    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    public LabelDto create(LabelDto newLabelDto) {
        return labelService.create(newLabelDto);
    }

    public void delete(Integer id) {
        labelService.deleteById(id);
    }

    public LabelDto findById(Integer id) {
        return labelService.findById(id);
    }

    public void update(LabelDto labelDto) {
        labelService.update(labelDto);
    }

    public LabelDto findByName(String name) {
        return labelService.findByName(name);
    }

    public List<LabelDto> findAll() {
        return labelService.findAll();
    }
}
