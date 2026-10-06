package edu.upc.campusnest.config;

import edu.upc.campusnest.model.District;
import edu.upc.campusnest.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

/** Carga inicial del catalogo de distritos (los del Capitulo I) si la tabla esta vacia. */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final DistrictRepository districtRepository;

    @Override
    public void run(String... args) {
        if (districtRepository.count() == 0) {
            List.of("San Miguel", "Jesus Maria", "Pueblo Libre", "Santiago de Surco", "Lince",
                    "Los Olivos", "La Molina", "Cercado de Lima", "Brena", "San Martin de Porres")
                .forEach(n -> districtRepository.save(District.builder().name(n).build()));
        }
    }
}
