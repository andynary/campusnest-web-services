package edu.upc.campusnest.dto.response;

import java.time.LocalDateTime;

public record MessageResponse(Long id, Long applicationId, Long senderId, String senderName,
                              String content, LocalDateTime sentAt) {}
