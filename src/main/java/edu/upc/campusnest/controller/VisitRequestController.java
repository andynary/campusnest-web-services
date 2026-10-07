package edu.upc.campusnest.controller;

import edu.upc.campusnest.dto.request.VisitProposalRequest;
import edu.upc.campusnest.dto.request.VisitStatusRequest;
import edu.upc.campusnest.dto.response.VisitResponse;
import edu.upc.campusnest.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** US 11: el SEEKER propone una visita; el HOST la aprueba o rechaza. */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VisitRequestController {
    private final VisitService visitService;

    @PostMapping("/rooms/{roomId}/visits")
    @PreAuthorize("hasRole('SEEKER')")
    @ResponseStatus(HttpStatus.CREATED)
    public VisitResponse propose(Authentication auth, @PathVariable Long roomId,
                                 @Valid @RequestBody VisitProposalRequest req) {
        return visitService.propose(auth.getName(), roomId, req.visitDateTime());
    }

    @GetMapping("/rooms/{roomId}/visits")
    @PreAuthorize("hasRole('HOST')")
    public List<VisitResponse> listByRoom(Authentication auth, @PathVariable Long roomId) {
        return visitService.listByRoom(auth.getName(), roomId);
    }

    @PatchMapping("/visits/{id}/status")
    @PreAuthorize("hasRole('HOST')")
    public VisitResponse changeStatus(Authentication auth, @PathVariable Long id,
                                      @Valid @RequestBody VisitStatusRequest req) {
        return visitService.changeStatus(auth.getName(), id, req.status());
    }
}
