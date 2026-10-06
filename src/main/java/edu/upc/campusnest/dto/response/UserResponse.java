package edu.upc.campusnest.dto.response;

import edu.upc.campusnest.model.Role;

public record UserResponse(Long id, String fullName, String email, Role role, boolean emailVerified) {}
