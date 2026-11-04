package edu.upc.campusnest.dto.response;

import edu.upc.campusnest.model.ApplicationStatus;
import java.time.LocalDateTime;

public record ApplicationResponse(
        Long id, 
        Long roomId, 
        String roomName,
        Long applicantId, 
        String applicantName,
        String university, 
        Integer cleanlinessLevel,
        Integer noiseLevel, 
        String sleepSchedule,
        String studyRoutine, 
        ApplicationStatus status,
        LocalDateTime createdAt
) {}
