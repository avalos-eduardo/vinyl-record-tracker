package com.example.vinyl_record_collection_tracker.controllers;

import com.example.vinyl_record_collection_tracker.dtos.VinylRequestDTO;
import com.example.vinyl_record_collection_tracker.dtos.VinylResponseDTO;
import com.example.vinyl_record_collection_tracker.services.VinylService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vinyls")
public class VinylController {

    private final VinylService vinylService;

    public VinylController(VinylService vinylService) {
        this.vinylService = vinylService;
    }

    @GetMapping
    public List<VinylResponseDTO> getAllVinyls() {
        return vinylService.getAllVinyls();
    }

    @GetMapping("/{id}")
    public VinylResponseDTO getVinyl(@PathVariable Long id) {
        return vinylService.getVinylById(id);
    }

    @GetMapping("/user/{userId}")
    public List<VinylResponseDTO> getVinylsByUser(@PathVariable Long userId) {
        return vinylService.getVinylsByUserId(userId);
    }

    @PostMapping
    public VinylResponseDTO createVinyl(@RequestBody VinylRequestDTO dto, @RequestParam Long userId) {
        return vinylService.createVinyl(dto, userId);
    }

    @PutMapping("/{id}")
    public VinylResponseDTO updateVinyl(@PathVariable Long id, @RequestBody VinylRequestDTO dto) {
        return vinylService.updateVinyl(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteVinyl(@PathVariable Long id) {
        vinylService.deleteVinyl(id);
    }
}