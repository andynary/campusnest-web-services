package edu.upc.campusnest.dto.response;

import edu.upc.campusnest.model.ApplicationStatus;
import java.time.LocalDateTime;

/** Fila del panel comparativo (US 07): datos academicos y habitos declarados del postulante. */
public record ApplicationResponse(Long id, Long roomId, String roomName,
                                  Long applicantId, String applicantName, String university,
                                  Integer cleanlinessLevel, Integer noiseLevel,
                                  String sleepSchedule, String studyRoutine,
                                  ApplicationStatus status, LocalDateTime createdAt) {}
