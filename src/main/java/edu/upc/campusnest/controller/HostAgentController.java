package edu.upc.campusnest.controller;

import edu.upc.campusnest.agent.dto.HostAgentReply;
import edu.upc.campusnest.agent.dto.HostAgentRequest;
import edu.upc.campusnest.agent.service.HostAgentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** US 24: chat del agente de IA para anfitriones. Solo rol HOST; el usuario sale del token. */
@RestController
@RequestMapping("/api/agent/host")
@RequiredArgsConstructor
@PreAuthorize("hasRole('HOST')")
public class HostAgentController {

    private final HostAgentService hostAgentService;

    @PostMapping("/chat")
    public HostAgentReply chat(Authentication auth, @Valid @RequestBody HostAgentRequest req) {
        return hostAgentService.chat(auth.getName(), req.message());
    }
}
