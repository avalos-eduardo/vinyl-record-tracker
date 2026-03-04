package com.example.vinyl_record_collection_tracker.services;

import com.example.vinyl_record_collection_tracker.models.User;
import com.example.vinyl_record_collection_tracker.models.Vinyl;
import com.example.vinyl_record_collection_tracker.repositories.UserRepository;
import com.example.vinyl_record_collection_tracker.repositories.VinylRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VinylService {

    private final VinylRepository vinylRepository;
    private final UserRepository userRepository;

    public VinylService(VinylRepository vinylRepository, UserRepository userRepository) {
        this.vinylRepository = vinylRepository;
        this.userRepository = userRepository;
    }

    public List<Vinyl> getAllVinyls() {
        return vinylRepository.findAll();
    }

    public Vinyl getVinylById(Long id) {
        return vinylRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vinyl not found."));
    }

    public List<Vinyl> getVinylsByUserId(Long userId) {
        return vinylRepository.findByUserId(userId);
    }

    public Vinyl createVinyl(Vinyl vinyl, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        vinyl.setUser(user);
        return vinylRepository.save(vinyl);
    }

    public Vinyl updateVinyl(Long id, Vinyl updatedVinyl) {
        Vinyl vinyl = vinylRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vinyl not found."));

        vinyl.setTitle(updatedVinyl.getTitle());
        vinyl.setArtist(updatedVinyl.getArtist());
        vinyl.setReleaseYear(updatedVinyl.getReleaseYear());

        return vinylRepository.save(vinyl);
    }

    public void deleteVinyl(Long id) {
        vinylRepository.deleteById(id);
    }
}