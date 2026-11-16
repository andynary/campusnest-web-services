package edu.upc.campusnest.agent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Mensaje que el anfitrion escribe en el chat del asistente. */
public record HostAgentRequest(@NotBlank @Size(max = 500) String message) {}
