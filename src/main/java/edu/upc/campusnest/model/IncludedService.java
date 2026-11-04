package edu.upc.campusnest.model;

import jakarta.persistence.*;
import lombok.*;

/** Servicio que puede estar incluido en el precio de una habitacion (agua, luz, wifi...). US 10. */
@Entity @Table(name = "included_services")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IncludedService {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
