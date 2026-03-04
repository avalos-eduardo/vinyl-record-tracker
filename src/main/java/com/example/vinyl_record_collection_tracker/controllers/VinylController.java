package com.example.vinyl_record_collection_tracker.controllers;

import com.example.vinyl_record_collection_tracker.models.User;
import com.example.vinyl_record_collection_tracker.models.Vinyl;
import com.example.vinyl_record_collection_tracker.repositories.UserRepository;
import com.example.vinyl_record_collection_tracker.repositories.VinylRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/vinyls")
public class VinylController {
    private final VinylRepository vinylRepository;
    private final UserRepository userRepository;

    public VinylController(VinylRepository vinylRepository, UserRepository userRepository) {
        this.vinylRepository = vinylRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Vinyl> getAllVinyls(){
        return vinylRepository.findAll();
    }

    @GetMapping("/{id}")
    public Vinyl getVinyl(@PathVariable Long id){
        return vinylRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vinyl not found."));
    }

    @GetMapping("/user/{userId}")
    public List<Vinyl> getVinylsByUser(@PathVariable Long userId) {
        return vinylRepository.findByUserId(userId);
    }

    @PostMapping
    public Vinyl createVinyl(@RequestBody Vinyl vinyl, @RequestParam Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        vinyl.setUser(user);
        return vinylRepository.save(vinyl);
    }

    @PutMapping("/{id}")
    public Vinyl updateVinyl(@PathVariable Long id, @RequestBody Vinyl updatedVinyl){
        Vinyl vinyl = vinylRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vinyl not found"));

        vinyl.setTitle(updatedVinyl.getTitle());
        vinyl.setArtist(updatedVinyl.getArtist());
        vinyl.setReleaseYear(updatedVinyl.getReleaseYear());

        return vinylRepository.save(vinyl);
    }

    @DeleteMapping("/{id}")
    public void deleteVinyl(@PathVariable Long id) {
        vinylRepository.deleteById(id);
    }
}
