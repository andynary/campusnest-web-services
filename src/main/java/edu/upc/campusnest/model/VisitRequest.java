package edu.upc.campusnest.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/** US 11: solicitud de visita propuesta por un SEEKER para una habitacion. */
@Entity @Table(name = "visit_requests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VisitRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(optional = false) @JoinColumn(name = "visitor_id")
    private User visitor;

    @Column(name = "visit_date_time", nullable = false)
    private LocalDateTime visitDateTime;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private VisitStatus status;
}
