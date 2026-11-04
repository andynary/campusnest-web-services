package edu.upc.campusnest.controller;

import edu.upc.campusnest.dto.response.DistrictResponse;
import edu.upc.campusnest.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Catalogo de distritos (para que el anfitrion elija donde queda su propiedad). */
@RestController
@RequestMapping("/api/districts")
@RequiredArgsConstructor
public class DistrictController {
    private final DistrictRepository districtRepository;

    @GetMapping
    public List<DistrictResponse> getAll() {
        return districtRepository.findAll().stream()
                .map(d -> new DistrictResponse(d.getId(), d.getName()))
                .toList();
    }
}
