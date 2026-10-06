package edu.upc.campusnest.dto.response;

import edu.upc.campusnest.model.RoomStatus;
import java.math.BigDecimal;

public record RoomResponse(Long id, Long propertyId, String name, String description,
                           BigDecimal monthlyPrice, boolean furnished, boolean privateBathroom,
                           RoomStatus status) {}
