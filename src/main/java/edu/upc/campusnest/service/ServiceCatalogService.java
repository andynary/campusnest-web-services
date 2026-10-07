package edu.upc.campusnest.service;

import edu.upc.campusnest.dto.response.ServiceResponse;
import edu.upc.campusnest.repository.IncludedServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/** US 10: catalogo de servicios incluidos. */
@Service
@RequiredArgsConstructor
public class ServiceCatalogService {
    private final IncludedServiceRepository serviceRepository;

    @Transactional(readOnly = true)
    public List<ServiceResponse> listAll() {
        return serviceRepository.findAll().stream()
                .map(s -> new ServiceResponse(s.getId(), s.getName())).toList();
    }
}
