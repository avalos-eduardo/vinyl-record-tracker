package com.example.vinyl_record_collection_tracker.controllers;

import com.example.vinyl_record_collection_tracker.dtos.DiscogsSearchResultDTO;
import com.example.vinyl_record_collection_tracker.services.DiscogsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discogs")
public class DiscogsController {

    private final DiscogsService discogsService;

    public DiscogsController(DiscogsService discogsService) {
        this.discogsService = discogsService;
    }

    @GetMapping("/search")
    public List<DiscogsSearchResultDTO> search(@RequestParam String query, @RequestParam(defaultValue = "1") int page) {
        return discogsService.search(query, page);
    }
}