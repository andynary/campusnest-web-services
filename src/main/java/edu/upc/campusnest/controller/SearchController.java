package edu.upc.campusnest.controller;

import edu.upc.campusnest.dto.response.RoomSearchResponse;
import edu.upc.campusnest.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/rooms/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<List<RoomSearchResponse>> searchRooms(
            @RequestParam(required = false) String district,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer cleanlinessLevel,
            @RequestParam(required = false) Integer noiseLevel
    ) {
        return ResponseEntity.ok(searchService.searchRooms(district, maxPrice, cleanlinessLevel, noiseLevel));
    }
}