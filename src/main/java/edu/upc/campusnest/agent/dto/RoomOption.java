package edu.upc.campusnest.agent.dto;

import java.math.BigDecimal;

/** Habitacion del anfitrion tal como la ve el agente (DTO propio del agente, no el del resto del proyecto). */
public record RoomOption(Long roomId, String roomName, String propertyTitle, String district,
                         String status, BigDecimal monthlyPrice) {}
