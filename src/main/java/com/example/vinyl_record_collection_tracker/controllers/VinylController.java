package com.example.vinyl_record_collection_tracker.controllers;

import com.example.vinyl_record_collection_tracker.models.Vinyl;
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
    public List<Vinyl> getAllVinyls() {
        return vinylService.getAllVinyls();
    }

    @GetMapping("/{id}")
    public Vinyl getVinyl(@PathVariable Long id) {
        return vinylService.getVinylById(id);
    }

    @GetMapping("/user/{userId}")
    public List<Vinyl> getVinylsByUser(@PathVariable Long userId) {
        return vinylService.getVinylsByUserId(userId);
    }

    @PostMapping
    public Vinyl createVinyl(@RequestBody Vinyl vinyl, @RequestParam Long userId) {
        return vinylService.createVinyl(vinyl, userId);
    }

    @PutMapping("/{id}")
    public Vinyl updateVinyl(@PathVariable Long id, @RequestBody Vinyl updatedVinyl) {
        return vinylService.updateVinyl(id, updatedVinyl);
    }

    @DeleteMapping("/{id}")
    public void deleteVinyl(@PathVariable Long id) {
        vinylService.deleteVinyl(id);
    }
}