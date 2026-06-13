package com.example.vinyl_record_collection_tracker.controllers;

import com.example.vinyl_record_collection_tracker.dtos.DiscogsMasterResponseDTO;
import com.example.vinyl_record_collection_tracker.dtos.PriceHistoryResponseDTO;
import com.example.vinyl_record_collection_tracker.dtos.UserVinylRequestDTO;
import com.example.vinyl_record_collection_tracker.dtos.UserVinylResponseDTO;
import com.example.vinyl_record_collection_tracker.services.PriceHistoryService;
import com.example.vinyl_record_collection_tracker.services.UserVinylService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collection")
public class UserVinylController {

    private final UserVinylService userVinylService;
    private final PriceHistoryService priceHistoryService;

    public UserVinylController(UserVinylService userVinylService,
                               PriceHistoryService priceHistoryService) {
        this.userVinylService = userVinylService;
        this.priceHistoryService = priceHistoryService;
    }

    @GetMapping
    public List<UserVinylResponseDTO> getCollection() {
        return userVinylService.getCurrentUserCollection();
    }

    @GetMapping("/masters")
    public List<DiscogsMasterResponseDTO> getMasters() {
        return userVinylService.getCurrentUserMasters();
    }

    @GetMapping("/masters/{masterId}/releases")
    public List<UserVinylResponseDTO> getReleasesForMaster(@PathVariable Long masterId) {
        return userVinylService.getReleasesForMaster(masterId);
    }

    @GetMapping("/masters/{masterId}/releases/{id}")
    public UserVinylResponseDTO getUserVinyl(@PathVariable Long masterId,
                                             @PathVariable Long id) {
        return userVinylService.getUserVinylById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserVinylResponseDTO addVinyl(@RequestBody UserVinylRequestDTO dto) {
        return userVinylService.addVinyl(dto);
    }

    @PutMapping("/{id}")
    public UserVinylResponseDTO updateUserVinyl(@PathVariable Long id,
                                                @RequestBody UserVinylRequestDTO dto) {
        return userVinylService.updateUserVinyl(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserVinyl(@PathVariable Long id) {
        userVinylService.deleteUserVinyl(id);
    }

    @PostMapping("/{id}/refresh-price")
    public PriceHistoryResponseDTO refreshPrice(@PathVariable Long id) {
        Long discogsReleaseId = userVinylService.getDiscogsReleaseIdForUserVinyl(id);
        return priceHistoryService.refreshPrice(discogsReleaseId);
    }
}