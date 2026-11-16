package edu.upc.campusnest.agent.service;

import edu.upc.campusnest.agent.dto.HostAgentReply;
import edu.upc.campusnest.agent.tools.AgentActions;
import edu.upc.campusnest.model.User;
import edu.upc.campusnest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Recibe el mensaje del anfitrion, arma la peticion (prompt + memoria) y devuelve la respuesta.
 * El modelo responde TEXTO; el DTO HostAgentReply lo arma el backend. (Con herramientas activas,
 * el modelo de Groq falla si ademas se le exige un JSON estricto con .entity()).
 */
@Service
public class HostAgentService {

    private static final Logger log = LoggerFactory.getLogger(HostAgentService.class);

    private final ChatClient chatClient;
    private final UserRepository userRepository;

    public HostAgentService(@Qualifier("hostAgentChatClient") ChatClient chatClient,
                            UserRepository userRepository) {
        this.chatClient = chatClient;
        this.userRepository = userRepository;
    }

    public HostAgentReply chat(String hostEmail, String message) {
        String name = userRepository.findByEmail(hostEmail).map(User::getFullName).orElse("anfitrion");
        AgentActions.reset();
        try {
            String text = chatClient.prompt()
                    .system(s -> s.param("today", LocalDate.now().toString()).param("username", name))
                    .user(message)
                    // un hilo de conversacion por usuario (la memoria nunca se mezcla entre personas)
                    .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "host:" + hostEmail))
                    .call()
                    .content();
            boolean changed = AgentActions.consumeRoomsChanged();
            if (text == null || text.isBlank()) {
                return new HostAgentReply("No obtuve respuesta del asistente. Revisa tu panel o intenta nuevamente.", changed);
            }
            return new HostAgentReply(text.trim(), changed);
        } catch (Exception e) {
            log.warn("Fallo el agente del anfitrion: {}", e.toString());
            return new HostAgentReply(
                    "No pude completar tu solicitud con el asistente. Revisa el estado en tu panel o intenta nuevamente.",
                    AgentActions.consumeRoomsChanged());
        }
    }
}
