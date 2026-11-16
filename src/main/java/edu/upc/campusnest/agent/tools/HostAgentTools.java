package edu.upc.campusnest.agent.tools;

import edu.upc.campusnest.agent.dto.RoomOption;
import edu.upc.campusnest.agent.dto.RoomStatusResult;
import edu.upc.campusnest.dto.response.RoomResponse;
import edu.upc.campusnest.exception.BusinessRuleException;
import edu.upc.campusnest.exception.ResourceNotFoundException;
import edu.upc.campusnest.model.RoomStatus;
import edu.upc.campusnest.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

/**
 * Herramientas del agente del anfitrion. SIN logica de negocio: solo puentes hacia PropertyService.
 * El anfitrion NUNCA viene del modelo: se toma del token (SecurityContext), asi el agente no puede
 * actuar sobre habitaciones de otra persona aunque se le pida.
 */
@Component
@RequiredArgsConstructor
public class HostAgentTools {

    private final PropertyService propertyService;

    @Tool(name = "list_my_rooms", description = """
            Lista TODAS las habitaciones del anfitrion que esta conversando: id, nombre, propiedad (titulo), \
            distrito, estado (AVAILABLE, OCCUPIED, HIDDEN o SUSPENDED) y precio mensual. \
            Usala siempre antes de cambiar el estado de una habitacion, para identificar cual es.""")
    public List<RoomOption> listMyRooms() {
        String email = currentHostEmail();
        return propertyService.myProperties(email).stream()
                .flatMap(p -> p.rooms().stream().map(r -> toOption(r, p.title(), p.district())))
                .toList();
    }

    @Tool(name = "update_room_status", description = """
            Cambia el estado de UNA habitacion del anfitrion. Por ahora solo admite el estado OCCUPIED \
            (marcar como ocupada), lo que la retira de la busqueda publica. \
            Usala solo cuando ya tengas identificada UNA habitacion concreta (id obtenido con list_my_rooms).""")
    public RoomStatusResult updateRoomStatus(
            @ToolParam(description = "Id numerico de la habitacion, tal como lo devolvio list_my_rooms") Long roomId,
            @ToolParam(description = "Nuevo estado. Solo se admite OCCUPIED") String status) {
        try {
            String email = currentHostEmail();
            if (!isOccupied(status)) {
                return RoomStatusResult.fail("UNSUPPORTED_STATUS",
                        "Por ahora solo se puede marcar una habitacion como ocupada (OCCUPIED)");
            }
            RoomResponse updated = propertyService.markRoomOccupied(email, roomId);
            AgentActions.markRoomsChanged();
            return RoomStatusResult.ok(toOption(updated, null, updated.district()));
        } catch (BusinessRuleException e) {
            return RoomStatusResult.fail("RULE", e.getMessage());
        } catch (ResourceNotFoundException e) {
            return RoomStatusResult.fail("NOT_FOUND", e.getMessage());
        } catch (AccessDeniedException e) {
            return RoomStatusResult.fail("FORBIDDEN", e.getMessage());
        }
    }

    private static boolean isOccupied(String status) {
        if (status == null) return false;
        String s = status.trim().toUpperCase(Locale.ROOT);
        return s.equals(RoomStatus.OCCUPIED.name()) || s.equals("OCUPADA");
    }

    private static RoomOption toOption(RoomResponse r, String propertyTitle, String district) {
        return new RoomOption(r.id(), r.name(), propertyTitle, district, r.status().name(), r.monthlyPrice());
    }

    /** Correo del usuario logueado; solo el rol HOST puede usar estas herramientas. */
    private static String currentHostEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isHost = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_HOST"));
        if (!isHost) {
            throw new AccessDeniedException("Solo un anfitrion puede usar estas herramientas");
        }
        return auth.getName();
    }
}
