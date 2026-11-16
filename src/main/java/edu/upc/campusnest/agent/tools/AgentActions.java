package edu.upc.campusnest.agent.tools;

/**
 * Registra, durante UNA peticion al agente, si alguna herramienta cambio datos.
 * Asi "roomsChanged" lo decide el backend (hecho real) y no el texto que escriba el modelo.
 * Funciona porque las herramientas se ejecutan en el mismo hilo de la peticion (llamada .call()).
 */
public final class AgentActions {

    private static final ThreadLocal<Boolean> ROOMS_CHANGED = ThreadLocal.withInitial(() -> false);

    private AgentActions() {}

    public static void markRoomsChanged() {
        ROOMS_CHANGED.set(true);
    }

    /** Devuelve si hubo cambios y limpia el registro. */
    public static boolean consumeRoomsChanged() {
        boolean changed = ROOMS_CHANGED.get();
        ROOMS_CHANGED.remove();
        return changed;
    }

    public static void reset() {
        ROOMS_CHANGED.remove();
    }
}
