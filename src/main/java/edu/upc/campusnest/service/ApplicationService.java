package edu.upc.campusnest.service;

import edu.upc.campusnest.dto.response.ApplicationResponse;
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

/** US 07: postulaciones a habitaciones y panel comparativo para el anfitrion. */
@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final StudentProfileRepository profileRepository;

    /** El postulante sale del token (no de la URL). */
    @Transactional
    public ApplicationResponse apply(String applicantEmail, Long roomId) {
        Room room = findRoom(roomId);
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new BusinessRuleException("La habitacion no esta disponible para postular");
        }
        User applicant = userRepository.findByEmail(applicantEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        if (applicationRepository.existsByRoomIdAndApplicantId(roomId, applicant.getId())) {
            throw new BusinessRuleException("Ya postulaste a esta habitacion");
        }
        Application saved = applicationRepository.save(Application.builder()
                .room(room).applicant(applicant)
                .status(ApplicationStatus.PENDING)
                .createdAt(LocalDateTime.now()).build());
        return toResponse(saved);
    }

    /** Panel comparativo: solo el anfitrion duenio de la habitacion. */
    @Transactional(readOnly = true)
    public List<ApplicationResponse> listByRoom(String hostEmail, Long roomId) {
        Room room = findRoom(roomId);
        assertOwner(room, hostEmail);
        return applicationRepository.findByRoomId(roomId).stream().map(this::toResponse).toList();
    }

    @Transactional
    public ApplicationResponse changeStatus(String hostEmail, Long applicationId, ApplicationStatus newStatus) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Postulacion no encontrada"));
        assertOwner(app.getRoom(), hostEmail);
        if (newStatus == ApplicationStatus.PENDING) {
            throw new BusinessRuleException("El nuevo estado debe ser ACCEPTED o REJECTED");
        }
        if (app.getStatus() != ApplicationStatus.PENDING) {
            throw new BusinessRuleException("La postulacion ya fue resuelta");
        }
        if (newStatus == ApplicationStatus.ACCEPTED && app.getRoom().getStatus() != RoomStatus.AVAILABLE) {
            throw new BusinessRuleException("La habitacion ya no esta disponible");
        }
        app.setStatus(newStatus);
        return toResponse(applicationRepository.save(app));
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

    private ApplicationResponse toResponse(Application a) {
        User u = a.getApplicant();
        StudentProfile p = profileRepository.findByUserId(u.getId()).orElse(null);
        return new ApplicationResponse(a.getId(), a.getRoom().getId(), a.getRoom().getName(),
                u.getId(), u.getFullName(),
                p != null ? p.getUniversity() : null,
                p != null ? p.getCleanlinessLevel() : null,
                p != null ? p.getNoiseLevel() : null,
                p != null ? p.getSleepSchedule() : null,
                p != null ? p.getStudyRoutine() : null,
                a.getStatus(), a.getCreatedAt());
    }
}
