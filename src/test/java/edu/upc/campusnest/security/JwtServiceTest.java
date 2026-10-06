package edu.upc.campusnest.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private final JwtService service =
            new JwtService("clave-de-prueba-de-al-menos-32-caracteres!!", 60_000);

    @Test
    void generaYLeeElToken() {
        String token = service.generateToken("host@upc.edu.pe", "HOST");
        assertEquals("host@upc.edu.pe", service.extractEmail(token));
    }

    @Test
    void rechazaUnTokenAlterado() {
        String token = service.generateToken("host@upc.edu.pe", "HOST");
        assertThrows(Exception.class, () -> service.extractEmail(token + "x"));
    }
}
