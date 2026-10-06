package edu.upc.campusnest.controller;

import edu.upc.campusnest.dto.request.PropertyRequest;
import edu.upc.campusnest.dto.request.RoomRequest;
import edu.upc.campusnest.dto.request.RoomStatusRequest;
import edu.upc.campusnest.dto.response.PropertyResponse;
import edu.upc.campusnest.dto.response.RoomResponse;
import edu.upc.campusnest.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** US 03: solo el rol HOST puede gestionar propiedades y habitaciones. */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@PreAuthorize("hasRole('HOST')")
public class PropertyController {
    private final PropertyService propertyService;

    @PostMapping("/properties")
    @ResponseStatus(HttpStatus.CREATED)
    public PropertyResponse create(Authentication auth, @Valid @RequestBody PropertyRequest req) {
        return propertyService.createProperty(auth.getName(), req);
    }

    @GetMapping("/properties/mine")
    public List<PropertyResponse> mine(Authentication auth) {
        return propertyService.myProperties(auth.getName());
    }

    @PostMapping("/properties/{propertyId}/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse addRoom(Authentication auth, @PathVariable Long propertyId,
                                @Valid @RequestBody RoomRequest req) {
        return propertyService.addRoom(auth.getName(), propertyId, req);
    }

    @PatchMapping("/rooms/{roomId}/status")
    public RoomResponse changeStatus(Authentication auth, @PathVariable Long roomId,
                                     @Valid @RequestBody RoomStatusRequest req) {
        return propertyService.changeRoomStatus(auth.getName(), roomId, req);
    }
}
