package edu.upc.campusnest.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/** US 07: postulacion de un SEEKER a una habitacion. */
@Entity @Table(name = "applications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Application {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(optional = false) @JoinColumn(name = "applicant_id")
    private User applicant;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ApplicationStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
