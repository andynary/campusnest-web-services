package edu.upc.campusnest.dto.response;

import edu.upc.campusnest.model.VisitStatus;
import java.time.LocalDateTime;

public record VisitResponse(Long id, Long roomId, String roomName,
                            Long visitorId, String visitorName,
                            LocalDateTime visitDateTime, VisitStatus status) {}
