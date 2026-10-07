package edu.upc.campusnest.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record VisitProposalRequest(
        @NotNull @Future(message = "La fecha de visita debe ser futura") LocalDateTime visitDateTime) {}
