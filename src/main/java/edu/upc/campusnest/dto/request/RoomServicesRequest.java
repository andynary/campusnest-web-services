package edu.upc.campusnest.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/** Ids de los servicios incluidos en una habitacion (reemplaza la lista anterior). */
public record RoomServicesRequest(@NotNull List<Long> serviceIds) {}
