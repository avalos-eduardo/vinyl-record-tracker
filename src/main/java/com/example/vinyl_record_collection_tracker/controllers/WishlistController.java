package com.example.vinyl_record_collection_tracker.controllers;

import com.example.vinyl_record_collection_tracker.dtos.DiscogsMasterResponseDTO;
import com.example.vinyl_record_collection_tracker.dtos.UserVinylRequestDTO;
import com.example.vinyl_record_collection_tracker.dtos.UserVinylResponseDTO;
import com.example.vinyl_record_collection_tracker.models.VinylCondition;
import com.example.vinyl_record_collection_tracker.services.UserVinylService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {
    private final UserVinylService userVinylService;

    public WishlistController(UserVinylService userVinylService) {
        this.userVinylService = userVinylService;
    }

    @GetMapping("/masters")
    public List<DiscogsMasterResponseDTO> getWishlistMasters() {
        return userVinylService.getCurrentUserWishlistMasters();
    }

    @GetMapping("/masters/{masterId}/releases")
    public List<UserVinylResponseDTO> getWishlistReleasesForMaster(
            @PathVariable Long masterId) {
        return userVinylService.getWishlistReleasesForMaster(masterId);
    }

    @GetMapping("/masters/{masterId}/releases/{id}")
    public UserVinylResponseDTO getWishlistVinyl(@PathVariable Long masterId,
                                                 @PathVariable Long id) {
        return userVinylService.getUserVinylById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserVinylResponseDTO addToWishlist(@RequestBody UserVinylRequestDTO dto) {
        dto.setWishlist(true);
        return userVinylService.addVinyl(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromWishlist(@PathVariable Long id) {
        userVinylService.deleteUserVinyl(id);
    }

    @PostMapping("/{id}/move-to-collection")
    public UserVinylResponseDTO moveToCollection(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        VinylCondition condition = VinylCondition.valueOf(body.get("condition"));
        return userVinylService.moveToCollection(id, condition);
    }
}