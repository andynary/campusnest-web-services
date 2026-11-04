package edu.upc.campusnest.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Regla de compatibilidad de la demo: linea base limpieza=4, ruido=2 (escala 1-5). */
class SearchServiceTest {

    @Test
    void coincideConLaLineaBaseEs100() {
        assertEquals(100, SearchService.score(4, 2, 4, 2));
    }

    @Test
    void peorCasoConLaBaseActualEs25() {
        // limpieza 1 (a 3 de la base) + ruido 5 (a 3 de la base) = 6 de 8 puntos
        assertEquals(25, SearchService.score(1, 5, 4, 2));
    }

    @Test
    void perfilNormalEs75() {
        assertEquals(75, SearchService.score(3, 3, 4, 2));
    }

    @Test
    void sinPerfilUsaValorNeutro() {
        assertEquals(SearchService.score(3, 3, 4, 2), SearchService.score(null, null, 4, 2));
    }

    @Test
    void nivelesFueraDeEscalaSeLimitan() {
        assertEquals(SearchService.score(5, 1, 4, 2), SearchService.score(99, -7, 4, 2));
    }
}
