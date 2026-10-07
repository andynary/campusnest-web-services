package edu.upc.campusnest.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/** Habitacion independiente de una propiedad, con precio, estado y servicios propios (US 03 y US 10). */
@Entity @Table(name = "rooms")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "property_id")
    private Property property;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "monthly_price", nullable = false)
    private BigDecimal monthlyPrice;

    private boolean furnished;

    @Column(name = "private_bathroom")
    private boolean privateBathroom;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private RoomStatus status;

    /** US 10: servicios incluidos en el alquiler (agua, luz, internet...). */
    @ManyToMany
    @JoinTable(name = "room_services",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    @Builder.Default
    private Set<IncludedService> services = new HashSet<>();
}
