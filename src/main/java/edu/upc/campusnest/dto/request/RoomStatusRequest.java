package edu.upc.campusnest.dto.request;

import edu.upc.campusnest.model.RoomStatus;
import jakarta.validation.constraints.NotNull;

public record RoomStatusRequest(@NotNull RoomStatus status) {}
