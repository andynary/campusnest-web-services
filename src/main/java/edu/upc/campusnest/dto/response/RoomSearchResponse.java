package edu.upc.campusnest.dto.response;

import edu.upc.campusnest.model.RoomStatus;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomSearchResponse {
    private Long id;
    private Long propertyId;
    private String name;
    private String description;
    private BigDecimal monthlyPrice;
    private boolean furnished;
    private boolean privateBathroom;
    private RoomStatus status;
    private String district;
    private int compatibilityScore; // porcentaje US 01 (ej. 85, 92)
}