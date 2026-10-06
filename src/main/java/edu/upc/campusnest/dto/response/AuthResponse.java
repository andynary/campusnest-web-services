package edu.upc.campusnest.dto.response;

public record AuthResponse(String token, Long userId, String fullName, String email, String role) {}
