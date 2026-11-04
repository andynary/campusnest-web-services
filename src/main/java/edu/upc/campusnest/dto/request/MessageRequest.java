package edu.upc.campusnest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageRequest(@NotBlank @Size(max = 1000, message = "El mensaje no puede pasar de 1000 caracteres") String content) {}
