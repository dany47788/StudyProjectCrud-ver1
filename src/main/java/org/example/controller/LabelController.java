package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.LabelDto;
import org.example.service.LabelService;

import java.util.List;

@AllArgsConstructor
public class LabelController {

    private final LabelService labelService;

    public List<LabelDto> findAll() {
        return labelService.findAll();
    }

    public LabelDto findById(Integer id) {
        return labelService.findById(id);
    }

    public LabelDto create(LabelDto newLabelDto) {
        return labelService.create(newLabelDto);
    }

    public void update(LabelDto labelDto) {
        labelService.update(labelDto);
    }

    public void deleteById(Integer id) {
        labelService.deleteById(id);
    }

    public LabelDto findByName(String name) {
        return labelService.findByName(name);
    }
}
