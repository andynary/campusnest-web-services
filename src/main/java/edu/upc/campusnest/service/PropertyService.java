package edu.upc.campusnest.service;

import edu.upc.campusnest.dto.request.PropertyRequest;
import edu.upc.campusnest.dto.request.RoomRequest;
import edu.upc.campusnest.dto.request.RoomStatusRequest;
import edu.upc.campusnest.dto.response.PropertyResponse;
import edu.upc.campusnest.dto.response.RoomResponse;
import edu.upc.campusnest.exception.ResourceNotFoundException;
import edu.upc.campusnest.mapper.PropertyMapper;
import edu.upc.campusnest.mapper.RoomMapper;
import edu.upc.campusnest.model.*;
import edu.upc.campusnest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/** US 03: propiedades y habitaciones independientes (logica de negocio). */
@Service
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final DistrictRepository districtRepository;
    private final PropertyMapper propertyMapper;
    private final RoomMapper roomMapper;

    @Transactional
    public PropertyResponse createProperty(String ownerEmail, PropertyRequest req) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        District district = districtRepository.findById(req.districtId())
                .orElseThrow(() -> new ResourceNotFoundException("Distrito no encontrado"));
        Property saved = propertyRepository.save(Property.builder()
                .owner(owner).district(district).title(req.title())
                .exactAddress(req.exactAddress()).createdAt(LocalDateTime.now()).build());
        return propertyMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PropertyResponse> myProperties(String ownerEmail) {
        return propertyRepository.findByOwnerEmail(ownerEmail).stream()
                .map(propertyMapper::toResponse).toList();
    }

    @Transactional
    public RoomResponse addRoom(String ownerEmail, Long propertyId, RoomRequest req) {
        Property property = ownedProperty(ownerEmail, propertyId);
        Room room = roomRepository.save(Room.builder()
                .property(property).name(req.name()).description(req.description())
                .monthlyPrice(req.monthlyPrice()).furnished(req.furnished())
                .privateBathroom(req.privateBathroom()).status(RoomStatus.AVAILABLE).build());
        return roomMapper.toResponse(room);
    }

    /** Ocultar / marcar ocupada UNA habitacion sin afectar las demas del predio. */
    @Transactional
    public RoomResponse changeRoomStatus(String ownerEmail, Long roomId, RoomStatusRequest req) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion no encontrada"));
        if (!room.getProperty().getOwner().getEmail().equals(ownerEmail)) {
            throw new AccessDeniedException("La habitacion no te pertenece");   // 403
        }
        room.setStatus(req.status());
        return roomMapper.toResponse(roomRepository.save(room));
    }

    private Property ownedProperty(String ownerEmail, Long propertyId) {
        Property p = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada"));
        if (!p.getOwner().getEmail().equals(ownerEmail)) {
            throw new AccessDeniedException("La propiedad no te pertenece");    // 403
        }
        return p;
    }
}
