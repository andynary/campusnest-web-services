package edu.upc.campusnest.controller;

import edu.upc.campusnest.dto.request.ApplicationStatusRequest;
import edu.upc.campusnest.dto.response.ApplicationResponse;
import edu.upc.campusnest.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** US 07: el SEEKER postula (identidad desde el token); el HOST compara y decide. */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping("/rooms/{roomId}/applications")
    @PreAuthorize("hasRole('SEEKER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse apply(Authentication auth, @PathVariable Long roomId) {
        return applicationService.apply(auth.getName(), roomId);
    }

    @GetMapping("/rooms/{roomId}/applications")
    @PreAuthorize("hasRole('HOST')")
    public List<ApplicationResponse> listByRoom(Authentication auth, @PathVariable Long roomId) {
        return applicationService.listByRoom(auth.getName(), roomId);
    }

    @PatchMapping("/applications/{id}/status")
    @PreAuthorize("hasRole('HOST')")
    public ApplicationResponse changeStatus(Authentication auth, @PathVariable Long id,
                                            @Valid @RequestBody ApplicationStatusRequest req) {
        return applicationService.changeStatus(auth.getName(), id, req.status());
    }
}
