package edu.upc.campusnest.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record VisitProposalRequest(@NotNull LocalDateTime visitDateTime) {}
