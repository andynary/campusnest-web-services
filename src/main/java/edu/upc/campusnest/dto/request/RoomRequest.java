package edu.upc.campusnest.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record RoomRequest(
        @NotBlank String name,
        String description,
        @NotNull @DecimalMin(value = "100.00", message = "El precio debe ser mayor a S/ 100") BigDecimal monthlyPrice,
        boolean furnished,
        boolean privateBathroom) {}
