package com.example.vinyl_record_collection_tracker.services;

import com.example.vinyl_record_collection_tracker.dtos.VinylRequestDTO;
import com.example.vinyl_record_collection_tracker.dtos.VinylResponseDTO;
import com.example.vinyl_record_collection_tracker.models.User;
import com.example.vinyl_record_collection_tracker.models.Vinyl;
import com.example.vinyl_record_collection_tracker.repositories.UserRepository;
import com.example.vinyl_record_collection_tracker.repositories.VinylRepository;
import com.example.vinyl_record_collection_tracker.security.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VinylService {

    private final VinylRepository vinylRepository;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    public VinylService(VinylRepository vinylRepository, UserRepository userRepository, AuthUtil authUtil) {
        this.vinylRepository = vinylRepository;
        this.userRepository = userRepository;
        this.authUtil = authUtil;
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
        User currentUser = authUtil.getCurrentUser();
        return vinylRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public VinylResponseDTO getVinylById(Long id) {
        User currentUser = authUtil.getCurrentUser();
        Vinyl vinyl = vinylRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vinyl not found."));

        if (!vinyl.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only view your own vinyls.");
        }

        return toDTO(vinyl);
    }

    public List<VinylResponseDTO> getVinylsByUserId(Long userId) {
        User currentUser = authUtil.getCurrentUser();

        if (!currentUser.getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only view your own vinyls.");
        }

        return vinylRepository.findByUserId(userId).stream().map(this::toDTO).toList();
    }

    public VinylResponseDTO createVinyl(VinylRequestDTO dto) {
        User currentUser = authUtil.getCurrentUser();

        Vinyl vinyl = new Vinyl();
        vinyl.setTitle(dto.getTitle());
        vinyl.setArtist(dto.getArtist());
        vinyl.setReleaseYear(dto.getReleaseYear());
        vinyl.setUser(currentUser);

        return toDTO(vinylRepository.save(vinyl));
    }

    public VinylResponseDTO updateVinyl(Long id, VinylRequestDTO dto) {
        User currentUser = authUtil.getCurrentUser();
        Vinyl vinyl = vinylRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vinyl not found."));

        if (!vinyl.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own vinyls.");
        }

        if (vinyl.getUser().getEmail().equals("demo@vinyltracker.com")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Demo account vinyls cannot be modified.");
        }

        vinyl.setTitle(dto.getTitle());
        vinyl.setArtist(dto.getArtist());
        vinyl.setReleaseYear(dto.getReleaseYear());

        return toDTO(vinylRepository.save(vinyl));
    }

    public void deleteVinyl(Long id) {
        User currentUser = authUtil.getCurrentUser();
        Vinyl vinyl = vinylRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vinyl not found."));

        if (!vinyl.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own vinyls.");
        }

        if (vinyl.getUser().getEmail().equals("demo@vinyltracker.com")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Demo account vinyls cannot be deleted.");
        }

        vinylRepository.deleteById(id);
    }
}