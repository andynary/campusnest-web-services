package edu.upc.campusnest.service;

import edu.upc.campusnest.dto.response.VisitResponse;
import edu.upc.campusnest.exception.BusinessRuleException;
import edu.upc.campusnest.exception.ResourceNotFoundException;
import edu.upc.campusnest.model.*;
import edu.upc.campusnest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/** US 11: el SEEKER propone fecha y hora de visita; el HOST la aprueba o rechaza. */
@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRequestRepository visitRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public VisitResponse propose(String visitorEmail, Long roomId, LocalDateTime visitDateTime) {
        Room room = findRoom(roomId);
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new BusinessRuleException("La habitacion no esta disponible para visitas");
        }
        User visitor = userRepository.findByEmail(visitorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        VisitRequest saved = visitRepository.save(VisitRequest.builder()
                .room(room).visitor(visitor).visitDateTime(visitDateTime)
                .status(VisitStatus.PROPOSED).build());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<VisitResponse> listByRoom(String hostEmail, Long roomId) {
        Room room = findRoom(roomId);
        assertOwner(room, hostEmail);
        return visitRepository.findByRoomId(roomId).stream().map(this::toResponse).toList();
    }

    @Transactional
    public VisitResponse changeStatus(String hostEmail, Long visitId, VisitStatus newStatus) {
        VisitRequest visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visita no encontrada"));
        assertOwner(visit.getRoom(), hostEmail);
        if (newStatus == VisitStatus.PROPOSED) {
            throw new BusinessRuleException("El nuevo estado debe ser APPROVED o REJECTED");
        }
        if (visit.getStatus() != VisitStatus.PROPOSED) {
            throw new BusinessRuleException("La visita ya fue resuelta");
        }
        if (newStatus == VisitStatus.APPROVED && visitRepository.existsByRoomIdAndVisitDateTimeAndStatus(
                visit.getRoom().getId(), visit.getVisitDateTime(), VisitStatus.APPROVED)) {
            throw new BusinessRuleException("Ese horario ya fue aprobado para otra visita");   // 409
        }
        visit.setStatus(newStatus);
        return toResponse(visitRepository.save(visit));
    }

    private Room findRoom(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion no encontrada"));
    }

    private void assertOwner(Room room, String hostEmail) {
        if (!room.getProperty().getOwner().getEmail().equals(hostEmail)) {
            throw new AccessDeniedException("La habitacion no te pertenece");   // 403
        }
    }

    private VisitResponse toResponse(VisitRequest v) {
        return new VisitResponse(v.getId(), v.getRoom().getId(), v.getRoom().getName(),
                v.getVisitor().getId(), v.getVisitor().getFullName(),
                v.getVisitDateTime(), v.getStatus());
    }
}
