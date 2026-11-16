package edu.upc.campusnest.agent.config;

import edu.upc.campusnest.agent.tools.HostAgentTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/** ChatClient del agente del anfitrion: personalidad (system prompt), herramientas y memoria. */
@Configuration
public class HostAgentConfig {

    @Bean
    public ChatClient hostAgentChatClient(ChatClient.Builder builder,
                                          ChatMemory chatMemory,
                                          HostAgentTools tools,
                                          @Value("classpath:/prompts/host-agent-system.md") Resource systemPrompt) {
        return builder
                .defaultSystem(read(systemPrompt))
                .defaultTools(tools)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    private static String read(Resource resource) {
        try {
            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer el system prompt del agente", e);
        }
    }
}
