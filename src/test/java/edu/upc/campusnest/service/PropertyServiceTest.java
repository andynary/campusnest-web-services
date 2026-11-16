package edu.upc.campusnest.service;

import edu.upc.campusnest.exception.BusinessRuleException;
import edu.upc.campusnest.exception.ResourceNotFoundException;
import edu.upc.campusnest.mapper.PropertyMapper;
import edu.upc.campusnest.mapper.RoomMapper;
import edu.upc.campusnest.model.Property;
import edu.upc.campusnest.model.Room;
import edu.upc.campusnest.model.RoomStatus;
import edu.upc.campusnest.model.User;
import edu.upc.campusnest.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** US 24: reglas de markRoomOccupied (las que usa la herramienta update_room_status del agente). */
@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {
    @Mock PropertyRepository propertyRepository;
    @Mock RoomRepository roomRepository;
    @Mock UserRepository userRepository;
    @Mock DistrictRepository districtRepository;
    @Mock IncludedServiceRepository serviceRepository;
    @Mock PropertyMapper propertyMapper;
    @Mock RoomMapper roomMapper;
    @InjectMocks PropertyService service;

    private Room roomOf(String ownerEmail, RoomStatus status) {
        User owner = User.builder().id(1L).email(ownerEmail).build();
        Property property = Property.builder().id(10L).owner(owner).build();
        return Room.builder().id(5L).name("Habitacion 1").property(property).status(status).build();
    }

    @Test
    void marcaOcupadaUnaHabitacionDisponible() {
        Room room = roomOf("ana@upc.edu.pe", RoomStatus.AVAILABLE);
        when(roomRepository.findById(5L)).thenReturn(Optional.of(room));
        when(roomRepository.save(room)).thenReturn(room);

        service.markRoomOccupied("ana@upc.edu.pe", 5L);

        assertEquals(RoomStatus.OCCUPIED, room.getStatus());
        verify(roomRepository).save(room);
    }

    @Test
    void avisaSiLaHabitacionYaEstabaOcupada() {
        Room room = roomOf("ana@upc.edu.pe", RoomStatus.OCCUPIED);
        when(roomRepository.findById(5L)).thenReturn(Optional.of(room));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.markRoomOccupied("ana@upc.edu.pe", 5L));

        assertEquals("La habitacion ya figuraba como ocupada", ex.getMessage());
        verify(roomRepository, never()).save(any());
    }

    @Test
    void noPermiteMarcarLaHabitacionDeOtroAnfitrion() {
        Room room = roomOf("ana@upc.edu.pe", RoomStatus.AVAILABLE);
        when(roomRepository.findById(5L)).thenReturn(Optional.of(room));

        assertThrows(AccessDeniedException.class, () -> service.markRoomOccupied("otro@upc.edu.pe", 5L));

        assertEquals(RoomStatus.AVAILABLE, room.getStatus());
        verify(roomRepository, never()).save(any());
    }

    @Test
    void fallaSiLaHabitacionNoExiste() {
        when(roomRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.markRoomOccupied("ana@upc.edu.pe", 99L));
    }
}
