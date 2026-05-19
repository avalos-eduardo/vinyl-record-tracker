package com.example.vinyl_record_collection_tracker.controllers;

import com.example.vinyl_record_collection_tracker.dtos.DiscogsSearchResultDTO;
import com.example.vinyl_record_collection_tracker.dtos.PriceHistoryResponseDTO;
import com.example.vinyl_record_collection_tracker.services.DiscogsService;
import com.example.vinyl_record_collection_tracker.services.PriceHistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discogs")
public class DiscogsController {

    private final DiscogsService discogsService;
    private final PriceHistoryService priceHistoryService;

    public DiscogsController(DiscogsService discogsService,
                             PriceHistoryService priceHistoryService) {
        this.discogsService = discogsService;
        this.priceHistoryService = priceHistoryService;
    }

    @GetMapping("/search")
    public List<DiscogsSearchResultDTO> search(@RequestParam String query) {
        return discogsService.search(query);
    }

    @GetMapping("/prices/{discogsReleaseId}")
    public List<PriceHistoryResponseDTO> getPriceHistory(@PathVariable Long discogsReleaseId) {
        return priceHistoryService.getPriceHistory(discogsReleaseId);
    }
}