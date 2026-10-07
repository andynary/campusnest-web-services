package edu.upc.campusnest.controller;

import edu.upc.campusnest.dto.response.ServiceResponse;
import edu.upc.campusnest.service.ServiceCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/** US 10: catalogo de servicios incluidos (cualquier usuario autenticado). */
@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceCatalogService catalogService;

    @GetMapping
    public List<ServiceResponse> getAll() {
        return catalogService.listAll();
    }
}
