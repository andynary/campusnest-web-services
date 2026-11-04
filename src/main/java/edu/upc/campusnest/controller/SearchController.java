package edu.upc.campusnest.controller;

import edu.upc.campusnest.dto.response.RoomResponse;
import edu.upc.campusnest.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<List<RoomResponse>> searchRooms(
            @RequestParam(required = false) Integer cleanlinessLevel,
            @RequestParam(required = false) Integer noiseLevel) {

        List<RoomResponse> rooms = searchService.searchRooms(cleanlinessLevel, noiseLevel);
        return ResponseEntity.ok(rooms);
    }
}