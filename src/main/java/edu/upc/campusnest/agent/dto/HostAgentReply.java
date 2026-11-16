package edu.upc.campusnest.agent.dto;

/**
 * Respuesta del endpoint del agente (la arma el backend).
 * message: texto del modelo que se muestra al anfitrion.
 * roomsChanged: lo calcula el backend (true solo si una herramienta cambio el estado de alguna
 * habitacion en este turno); el frontend lo usa para refrescar el panel.
 */
public record HostAgentReply(String message, boolean roomsChanged) {}
