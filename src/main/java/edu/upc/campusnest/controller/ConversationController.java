package edu.upc.campusnest.controller;

import edu.upc.campusnest.dto.request.MessageRequest;
import edu.upc.campusnest.dto.response.ConversationResponse;
import edu.upc.campusnest.dto.response.MessageResponse;
import edu.upc.campusnest.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Mensajes: disponible para HOST y SEEKER; el servicio verifica que sea participante. */
@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {
    private final MessageService messageService;

    @GetMapping
    public List<ConversationResponse> mine(Authentication auth) {
        return messageService.listConversations(auth.getName());
    }

    @GetMapping("/{applicationId}/messages")
    public List<MessageResponse> messages(Authentication auth, @PathVariable Long applicationId) {
        return messageService.listMessages(auth.getName(), applicationId);
    }

    @PostMapping("/{applicationId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse send(Authentication auth, @PathVariable Long applicationId,
                                @Valid @RequestBody MessageRequest req) {
        return messageService.send(auth.getName(), applicationId, req.content());
    }
}
