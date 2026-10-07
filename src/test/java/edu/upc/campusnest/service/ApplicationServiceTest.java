package edu.upc.campusnest.service;

import edu.upc.campusnest.exception.BusinessRuleException;
import edu.upc.campusnest.model.Room;
import edu.upc.campusnest.model.RoomStatus;
import edu.upc.campusnest.model.User;
import edu.upc.campusnest.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
    @Mock ApplicationRepository applicationRepository;
    @Mock RoomRepository roomRepository;
    @Mock UserRepository userRepository;
    @Mock StudentProfileRepository profileRepository;
    @InjectMocks ApplicationService service;

    @Test
    void noPermitePostularAUnaHabitacionOculta() {
        Room room = Room.builder().id(1L).status(RoomStatus.HIDDEN).build();
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        assertThrows(BusinessRuleException.class, () -> service.apply("maria@upc.edu.pe", 1L));
    }

    @Test
    void noPermitePostularDosVecesALaMismaHabitacion() {
        Room room = Room.builder().id(1L).status(RoomStatus.AVAILABLE).build();
        User user = User.builder().id(2L).email("maria@upc.edu.pe").build();
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(userRepository.findByEmail("maria@upc.edu.pe")).thenReturn(Optional.of(user));
        when(applicationRepository.existsByRoomIdAndApplicantId(1L, 2L)).thenReturn(true);
        assertThrows(BusinessRuleException.class, () -> service.apply("maria@upc.edu.pe", 1L));
    }
}
