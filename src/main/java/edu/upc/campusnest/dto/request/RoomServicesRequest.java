package edu.upc.campusnest.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RoomServicesRequest(@NotNull List<Long> serviceIds) {}
