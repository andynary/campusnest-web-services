package edu.upc.campusnest.service;

import edu.upc.campusnest.dto.response.RoomSearchResponse;
import edu.upc.campusnest.model.Room;
import edu.upc.campusnest.model.RoomStatus;
import edu.upc.campusnest.model.StudentProfile;
import edu.upc.campusnest.repository.RoomRepository;
import edu.upc.campusnest.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final RoomRepository roomRepository;
    private final StudentProfileRepository studentProfileRepository;

    @Transactional(readOnly = true)
    public List<RoomSearchResponse> searchRooms(String district, BigDecimal maxPrice, Integer userCleanliness, Integer userNoise) {
        // Criterio de aceptacion US 01: presupuesto valido mayor a S/ 100
        if (maxPrice != null && maxPrice.compareTo(new BigDecimal("100")) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Por favor, ingrese un presupuesto de alquiler válido mayor a S/ 100");
        }

        List<Room> rooms = roomRepository.findAll();

        return rooms.stream()
                .filter(r -> r.getStatus() == RoomStatus.AVAILABLE)
                .filter(r -> district == null || district.isBlank() || 
                        (r.getProperty().getDistrict() != null && r.getProperty().getDistrict().getName().equalsIgnoreCase(district.trim())))
                .filter(r -> maxPrice == null || r.getMonthlyPrice().compareTo(maxPrice) <= 0)
                .map(room -> {
                    int score = calculateCompatibility(room, userCleanliness, userNoise);
                    return RoomSearchResponse.builder()
                            .id(room.getId())
                            .propertyId(room.getProperty().getId())
                            .name(room.getName())
                            .description(room.getDescription())
                            .monthlyPrice(room.getMonthlyPrice())
                            .furnished(room.isFurnished())
                            .privateBathroom(room.isPrivateBathroom())
                            .status(room.getStatus())
                            .district(room.getProperty().getDistrict() != null ? room.getProperty().getDistrict().getName() : "")
                            .compatibilityScore(score)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private int calculateCompatibility(Room room, Integer userCleanliness, Integer userNoise) {
        if (userCleanliness == null && userNoise == null) {
            return 80; // Valor base neutral
        }

        // Obtener el perfil del anfitrion (owner de la propiedad)
        StudentProfile hostProfile = studentProfileRepository.findByUserId(room.getProperty().getOwner().getId()).orElse(null);

        if (hostProfile == null) {
            return 80;
        }

        int targetClean = userCleanliness != null ? userCleanliness : 3;
        int targetNoise = userNoise != null ? userNoise : 3;

        int hostClean = hostProfile.getCleanlinessLevel() != null ? hostProfile.getCleanlinessLevel() : 3;
        int hostNoise = hostProfile.getNoiseLevel() != null ? hostProfile.getNoiseLevel() : 3;

        int diffClean = Math.abs(hostClean - targetClean);
        int diffNoise = Math.abs(hostNoise - targetNoise);

        // Algoritmo de ponderacion: 100% menos diferencia de habitos
        int score = 100 - ((diffClean + diffNoise) * 10);
        return Math.max(score, 50);
    }
}