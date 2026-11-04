package edu.upc.campusnest.service;

import edu.upc.campusnest.dto.response.ConversationResponse;
import edu.upc.campusnest.dto.response.MessageResponse;
import edu.upc.campusnest.exception.BusinessRuleException;
import edu.upc.campusnest.exception.ResourceNotFoundException;
import edu.upc.campusnest.model.Application;
import edu.upc.campusnest.model.ApplicationStatus;
import edu.upc.campusnest.model.Message;
import edu.upc.campusnest.model.User;
import edu.upc.campusnest.repository.ApplicationRepository;
import edu.upc.campusnest.repository.MessageRepository;
import edu.upc.campusnest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Mensajes entre el anfitrion y el estudiante cuya postulacion fue ACEPTADA.
 * La identidad sale del token; solo los dos participantes pueden leer o escribir.
 */
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ConversationResponse> listConversations(String email) {
        Map<Long, Application> byId = new LinkedHashMap<>();
        applicationRepository.findByStatusAndApplicantEmail(ApplicationStatus.ACCEPTED, email)
                .forEach(a -> byId.put(a.getId(), a));
        applicationRepository.findByStatusAndRoom_Property_Owner_Email(ApplicationStatus.ACCEPTED, email)
                .forEach(a -> byId.put(a.getId(), a));
        return byId.values().stream()
                .map(a -> toConversation(a, email))
                .sorted(Comparator
                        .comparing(ConversationResponse::lastMessageAt,
                                Comparator.nullsLast(Comparator.<LocalDateTime>reverseOrder()))
                        .thenComparing(ConversationResponse::applicationId, Comparator.<Long>reverseOrder()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> listMessages(String email, Long applicationId) {
        Application app = accessibleConversation(email, applicationId);
        return messageRepository.findByApplicationIdOrderBySentAtAscIdAsc(app.getId()).stream()
                .map(this::toResponse).toList();
    }

    @Transactional
    public MessageResponse send(String email, Long applicationId, String content) {
        Application app = accessibleConversation(email, applicationId);
        User sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Message saved = messageRepository.save(Message.builder()
                .application(app).sender(sender)
                .content(content.trim()).sentAt(LocalDateTime.now()).build());
        return toResponse(saved);
    }

    /** 404 si no existe, 403 si no es participante, 409 si la postulacion no esta aceptada. */
    private Application accessibleConversation(String email, Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversacion no encontrada"));
        boolean isApplicant = app.getApplicant().getEmail().equals(email);
        boolean isHost = app.getRoom().getProperty().getOwner().getEmail().equals(email);
        if (!isApplicant && !isHost) {
            throw new AccessDeniedException("No participas en esta conversacion");   // 403
        }
        if (app.getStatus() != ApplicationStatus.ACCEPTED) {
            throw new BusinessRuleException("La conversacion se habilita cuando la solicitud es aceptada");   // 409
        }
        return app;
    }

    private ConversationResponse toConversation(Application a, String email) {
        boolean viewerIsApplicant = a.getApplicant().getEmail().equals(email);
        User other = viewerIsApplicant ? a.getRoom().getProperty().getOwner() : a.getApplicant();
        Message last = messageRepository.findFirstByApplicationIdOrderBySentAtDescIdDesc(a.getId()).orElse(null);
        return new ConversationResponse(a.getId(), a.getRoom().getId(), a.getRoom().getName(),
                other.getId(), other.getFullName(),
                last != null ? last.getContent() : null,
                last != null ? last.getSentAt() : null);
    }

    private MessageResponse toResponse(Message m) {
        return new MessageResponse(m.getId(), m.getApplication().getId(),
                m.getSender().getId(), m.getSender().getFullName(), m.getContent(), m.getSentAt());
    }
}
