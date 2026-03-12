package com.example.vinyl_record_collection_tracker.services;

import com.example.vinyl_record_collection_tracker.dtos.VinylRequestDTO;
import com.example.vinyl_record_collection_tracker.dtos.VinylResponseDTO;
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

    private VinylResponseDTO toDTO(Vinyl vinyl) {
        return new VinylResponseDTO(
                vinyl.getId(),
                vinyl.getTitle(),
                vinyl.getArtist(),
                vinyl.getReleaseYear(),
                vinyl.getUser().getId()
        );
    }

    public List<VinylResponseDTO> getAllVinyls() {
        return vinylRepository.findAll().stream().map(this::toDTO).toList();
    }

    public VinylResponseDTO getVinylById(Long id) {
        Vinyl vinyl = vinylRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vinyl not found."));
        return toDTO(vinyl);
    }

    public List<VinylResponseDTO> getVinylsByUserId(Long userId) {
        return vinylRepository.findByUserId(userId).stream().map(this::toDTO).toList();
    }

    public VinylResponseDTO createVinyl(VinylRequestDTO dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        Vinyl vinyl = new Vinyl();
        vinyl.setTitle(dto.getTitle());
        vinyl.setArtist(dto.getArtist());
        vinyl.setReleaseYear(dto.getReleaseYear());
        vinyl.setUser(user);

        return toDTO(vinylRepository.save(vinyl));
    }

    public VinylResponseDTO updateVinyl(Long id, VinylRequestDTO dto) {
        Vinyl vinyl = vinylRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vinyl not found."));

        vinyl.setTitle(dto.getTitle());
        vinyl.setArtist(dto.getArtist());
        vinyl.setReleaseYear(dto.getReleaseYear());

        return toDTO(vinylRepository.save(vinyl));
    }

    public void deleteVinyl(Long id) {
        vinylRepository.deleteById(id);
    }
}