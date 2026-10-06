package edu.upc.campusnest.dto.request;

import jakarta.validation.constraints.*;

public record PropertyRequest(
        @NotBlank String title,
        @NotBlank String exactAddress,
        @NotNull Long districtId) {}
