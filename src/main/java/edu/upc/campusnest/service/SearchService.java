package edu.upc.campusnest.service;

import edu.upc.campusnest.dto.response.RoomResponse;
import edu.upc.campusnest.mapper.RoomMapper;
import edu.upc.campusnest.model.RoomStatus;
import edu.upc.campusnest.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * US 01: busqueda de habitaciones disponibles + porcentaje de compatibilidad (regla de la demo).
 * El porcentaje compara los habitos del estudiante (limpieza y ruido, escala 1-5) contra una
 * linea base de convivencia ideal, igual para todas las habitaciones. Valores en application.yml.
 */
@Service
@RequiredArgsConstructor
public class SearchService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Value("${app.match.ideal-cleanliness:4}")
    private int idealCleanliness;

    @Value("${app.match.ideal-noise:2}")
    private int idealNoise;

    @Transactional(readOnly = true)
    public List<RoomResponse> searchRooms(Integer cleanlinessLevel, Integer noiseLevel) {
        int score = score(cleanlinessLevel, noiseLevel, idealCleanliness, idealNoise);
        return roomRepository.findAll().stream()
                .filter(room -> room.getStatus() == RoomStatus.AVAILABLE)
                .map(roomMapper::toResponse)
                .map(r -> new RoomResponse(r.id(), r.propertyId(), r.name(), r.description(),
                        r.monthlyPrice(), r.furnished(), r.privateBathroom(), r.status(),
                        r.services(), r.district(), score))
                .toList();
    }

    /**
     * 100% = coincide con la linea base; 0% = maxima diferencia posible (4 puntos por dimension).
     * Si el estudiante no tiene un nivel, se usa 3 (neutro). Los niveles se limitan a 1..5.
     */
    static int score(Integer cleanliness, Integer noise, int idealCleanliness, int idealNoise) {
        int c = clamp(cleanliness == null ? 3 : cleanliness);
        int n = clamp(noise == null ? 3 : noise);
        int diff = Math.abs(c - idealCleanliness) + Math.abs(n - idealNoise);
        return (int) Math.round(100.0 * (1 - diff / 8.0));
    }

    private static int clamp(int v) { return Math.max(1, Math.min(5, v)); }
}
