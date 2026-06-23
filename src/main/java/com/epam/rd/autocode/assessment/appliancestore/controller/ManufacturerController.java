package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer.CreateManufacturerRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer.ManufacturerResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.service.ManufacturerService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manufacturers")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    @GetMapping
    public List<ManufacturerResponseDto> getAllManufacturers() {
        return manufacturerService.getAll();
    }

    @GetMapping("/{id}")
    public ManufacturerResponseDto getManufacturerById(@PathVariable Long id) {
        return manufacturerService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ManufacturerResponseDto createManufacturer(
            @RequestBody @Valid CreateManufacturerRequestDto requestDto
    ) {
        return manufacturerService.create(requestDto);
    }

    @PutMapping("/{id}")
    public ManufacturerResponseDto updateManufacturer(
            @PathVariable Long id,
            @RequestBody @Valid CreateManufacturerRequestDto requestDto
    ) {
        return manufacturerService.updateById(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteManufacturer(@PathVariable Long id) {
        manufacturerService.deleteById(id);
    }
}
