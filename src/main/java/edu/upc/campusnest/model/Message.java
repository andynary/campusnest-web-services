package edu.upc.campusnest.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/** Mensaje dentro de la conversacion de una postulacion aceptada (anfitrion <-> estudiante). */
@Entity @Table(name = "messages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "application_id")
    private Application application;

    @ManyToOne(optional = false) @JoinColumn(name = "sender_id")
    private User sender;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;
}
