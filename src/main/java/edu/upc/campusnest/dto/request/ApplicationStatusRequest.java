package edu.upc.campusnest.dto.request;

import edu.upc.campusnest.model.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record ApplicationStatusRequest(@NotNull ApplicationStatus status) {}
