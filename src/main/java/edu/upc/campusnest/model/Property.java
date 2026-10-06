package edu.upc.campusnest.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Predio matriz del anfitrion (US 03). La direccion exacta NO se expone (Ley 29733). */
@Entity @Table(name = "properties")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Property {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(optional = false) @JoinColumn(name = "district_id")
    private District district;

    @Column(nullable = false)
    private String title;

    @Column(name = "exact_address", nullable = false)
    private String exactAddress;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Room> rooms = new ArrayList<>();
}
