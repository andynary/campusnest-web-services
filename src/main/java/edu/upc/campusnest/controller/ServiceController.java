package edu.upc.campusnest.controller;

import edu.upc.campusnest.model.IncludedService;
import edu.upc.campusnest.repository.IncludedServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final IncludedServiceRepository serviceRepository;

    @GetMapping
    public ResponseEntity<List<IncludedService>> getAllServices() {
        return ResponseEntity.ok(serviceRepository.findAll());
    }
}