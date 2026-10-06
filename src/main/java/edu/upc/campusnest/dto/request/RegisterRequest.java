package edu.upc.campusnest.dto.request;

import edu.upc.campusnest.model.Role;
import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank String fullName,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, message = "La clave debe tener al menos 6 caracteres") String password,
        @NotNull Role role,                 // SEEKER o HOST (ADMIN no se auto-registra)
        String university,
        @Min(1) @Max(5) Integer cleanlinessLevel,
        @Min(1) @Max(5) Integer noiseLevel,
        String sleepSchedule,
        String studyRoutine) {}
