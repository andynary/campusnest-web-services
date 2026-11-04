package edu.upc.campusnest.dto.response;

import java.time.LocalDateTime;

/** Una conversacion = una postulacion aceptada. otherUser* es la otra persona (no quien consulta). */
public record ConversationResponse(Long applicationId, Long roomId, String roomName,
                                   Long otherUserId, String otherUserName,
                                   String lastMessage, LocalDateTime lastMessageAt) {}
