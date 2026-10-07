package edu.upc.campusnest.controller;

import edu.upc.campusnest.model.VisitRequest;
import edu.upc.campusnest.repository.RoomRepository;
import edu.upc.campusnest.repository.UserRepository;
import edu.upc.campusnest.repository.VisitRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitRequestController {

    private final VisitRequestRepository visitRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<VisitRequest>> getVisitsByRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(visitRepository.findByRoomId(roomId));
    }

    @PostMapping("/room/{roomId}/visitor/{userId}")
    public ResponseEntity<VisitRequest> requestVisit(
            @PathVariable Long roomId,
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime visitDateTime
    ) {
        var room = roomRepository.findById(roomId).orElseThrow();
        var visitor = userRepository.findById(userId).orElseThrow();

        VisitRequest visit = VisitRequest.builder()
                .room(room)
                .visitor(visitor)
                .visitDateTime(visitDateTime)
                .status("PROPOSED")
                .build();

        return ResponseEntity.ok(visitRepository.save(visit));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<VisitRequest> updateStatus(@PathVariable Long id, @RequestParam String status) {
        VisitRequest visit = visitRepository.findById(id).orElseThrow();
        visit.setStatus(status.toUpperCase());
        return ResponseEntity.ok(visitRepository.save(visit));
    }
}
