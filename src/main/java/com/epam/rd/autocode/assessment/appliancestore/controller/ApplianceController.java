package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.ApplianceResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.ApplianceSearchParametersDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.CreateApplianceRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.service.ApplianceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/appliances")
public class ApplianceController {
    private final ApplianceService applianceService;

    @GetMapping
    public Page<ApplianceResponseDto> getAllAppliances(
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return applianceService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public ApplianceResponseDto getApplianceById(@PathVariable Long id) {
        return applianceService.getById(id);
    }

    @GetMapping("/search")
    public Page<ApplianceResponseDto> searchAppliances(
            ApplianceSearchParametersDto searchParameters,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return applianceService.search(searchParameters, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplianceResponseDto createAppliance(
            @RequestBody @Valid CreateApplianceRequestDto requestDto
    ) {
        return applianceService.create(requestDto);
    }

    @PutMapping("/{id}")
    public ApplianceResponseDto updateAppliance(
            @PathVariable Long id,
            @RequestBody @Valid CreateApplianceRequestDto requestDto
    ) {
        return applianceService.updateById(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppliance(@PathVariable Long id) {
        applianceService.deleteById(id);
    }
}
