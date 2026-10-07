package edu.upc.campusnest.dto.request;

import edu.upc.campusnest.model.VisitStatus;
import jakarta.validation.constraints.NotNull;

public record VisitStatusRequest(@NotNull VisitStatus status) {}
