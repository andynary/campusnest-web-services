package edu.upc.campusnest.agent.dto;

/**
 * Resultado de update_room_status para el agente.
 * code: OK | RULE | NOT_FOUND | FORBIDDEN | UNSUPPORTED_STATUS
 */
public record RoomStatusResult(boolean success, String code, String message, RoomOption room) {

    public static RoomStatusResult ok(RoomOption room) {
        return new RoomStatusResult(true, "OK", "Estado actualizado", room);
    }

    public static RoomStatusResult fail(String code, String message) {
        return new RoomStatusResult(false, code, message, null);
    }
}
