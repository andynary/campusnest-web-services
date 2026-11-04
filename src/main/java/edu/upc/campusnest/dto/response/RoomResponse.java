package edu.upc.campusnest.dto.response;

import edu.upc.campusnest.model.RoomStatus;
import java.math.BigDecimal;
import java.util.List;

/**
 * services: nombres de los servicios incluidos.
 * district: distrito de la propiedad (no la direccion exacta).
 * compatibilityScore: solo se llena en GET /api/rooms/search (0-100); en el resto es null.
 */
public record RoomResponse(Long id, Long propertyId, String name, String description,
                           BigDecimal monthlyPrice, boolean furnished, boolean privateBathroom,
                           RoomStatus status, List<String> services,
                           String district, Integer compatibilityScore) {}
