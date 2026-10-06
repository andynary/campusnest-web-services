package edu.upc.campusnest.dto.response;

import java.util.List;

/** Devuelve el distrito, no la direccion exacta (proteccion de datos). */
public record PropertyResponse(Long id, String title, String district, List<RoomResponse> rooms) {}
