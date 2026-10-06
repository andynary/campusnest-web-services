package edu.upc.campusnest.model;

import jakarta.persistence.*;
import lombok.*;

/** Habitos de convivencia (alimentan US 01 y US 07). Relacion 1:1 con users. */
@Entity @Table(name = "student_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StudentProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false) @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String university;

    @Column(name = "cleanliness_level") private Integer cleanlinessLevel; // 1..5
    @Column(name = "noise_level") private Integer noiseLevel;             // 1..5
    @Column(name = "sleep_schedule") private String sleepSchedule;
    @Column(name = "study_routine") private String studyRoutine;
}
