package edu.upc.campusnest.controller;

import edu.upc.campusnest.dto.response.ServiceResponse;
import edu.upc.campusnest.repository.IncludedServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Catalogo de servicios incluidos (para el paso 3 de "Publicar habitacion"). US 10. */
@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceCatalogController {
    private final IncludedServiceRepository serviceRepository;

    @GetMapping
    public List<ServiceResponse> getAll() {
        return serviceRepository.findAll().stream()
                .map(s -> new ServiceResponse(s.getId(), s.getName()))
                .toList();
    }
}
