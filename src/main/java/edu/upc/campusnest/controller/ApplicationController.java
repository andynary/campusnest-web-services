package edu.upc.campusnest.controller;

import edu.upc.campusnest.model.Application;
import edu.upc.campusnest.repository.ApplicationRepository;
import edu.upc.campusnest.repository.RoomRepository;
import edu.upc.campusnest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationRepository applicationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Application>> getApplicationsByRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(applicationRepository.findByRoomId(roomId));
    }

    @PostMapping("/room/{roomId}/applicant/{userId}")
    public ResponseEntity<Application> applyToRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        var room = roomRepository.findById(roomId).orElseThrow();
        var applicant = userRepository.findById(userId).orElseThrow();

        Application app = Application.builder()
                .room(room)
                .applicant(applicant)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(applicationRepository.save(app));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Application> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Application app = applicationRepository.findById(id).orElseThrow();
        app.setStatus(status.toUpperCase());
        return ResponseEntity.ok(applicationRepository.save(app));
    }
}
