package edu.upc.campusnest.config;

import edu.upc.campusnest.model.District;
import edu.upc.campusnest.model.IncludedService;
import edu.upc.campusnest.repository.DistrictRepository;
import edu.upc.campusnest.repository.IncludedServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

/** Carga inicial de catalogos (distritos del Capitulo I y servicios incluidos de US 10) si estan vacios. */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final DistrictRepository districtRepository;
    private final IncludedServiceRepository serviceRepository;

    @Override
    public void run(String... args) {
        if (districtRepository.count() == 0) {
            List.of("San Miguel", "Jesus Maria", "Pueblo Libre", "Santiago de Surco", "Lince",
                    "Los Olivos", "La Molina", "Cercado de Lima", "Brena", "San Martin de Porres")
                .forEach(n -> districtRepository.save(District.builder().name(n).build()));
        }
        if (serviceRepository.count() == 0) {
            List.of("Agua", "Luz", "Internet", "Gas", "Limpieza", "Cable")
                .forEach(n -> serviceRepository.save(IncludedService.builder().name(n).build()));
        }
    }
}
