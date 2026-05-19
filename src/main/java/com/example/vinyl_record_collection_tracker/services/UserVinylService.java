package com.example.vinyl_record_collection_tracker.services;

import com.example.vinyl_record_collection_tracker.dtos.DiscogsReleaseResponseDTO;
import com.example.vinyl_record_collection_tracker.dtos.UserVinylRequestDTO;
import com.example.vinyl_record_collection_tracker.dtos.UserVinylResponseDTO;
import com.example.vinyl_record_collection_tracker.models.DiscogsRelease;
import com.example.vinyl_record_collection_tracker.models.User;
import com.example.vinyl_record_collection_tracker.models.UserVinyl;
import com.example.vinyl_record_collection_tracker.repositories.UserVinylRepository;
import com.example.vinyl_record_collection_tracker.security.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserVinylService {

    private final UserVinylRepository userVinylRepository;
    private final DiscogsService discogsService;
    private final AuthUtil authUtil;

    public UserVinylService(UserVinylRepository userVinylRepository,
                            DiscogsService discogsService,
                            AuthUtil authUtil) {
        this.userVinylRepository = userVinylRepository;
        this.discogsService = discogsService;
        this.authUtil = authUtil;
    }

    private DiscogsReleaseResponseDTO toReleaseDTO(DiscogsRelease release) {
        return new DiscogsReleaseResponseDTO(
                release.getId(),
                release.getDiscogsId(),
                release.getTitle(),
                release.getArtist(),
                release.getLabel(),
                release.getGenre(),
                release.getFormat(),
                release.getReleaseYear(),
                release.getImageUrl(),
                release.getLastSyncedAt()
        );
    }

    private UserVinylResponseDTO toDTO(UserVinyl userVinyl) {
        return new UserVinylResponseDTO(
                userVinyl.getId(),
                userVinyl.getCondition(),
                userVinyl.getNotes(),
                userVinyl.getAddedAt(),
                toReleaseDTO(userVinyl.getDiscogsRelease())
        );
    }

    public List<UserVinylResponseDTO> getCurrentUserCollection() {
        User currentUser = authUtil.getCurrentUser();
        return userVinylRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public UserVinylResponseDTO getUserVinylById(Long id) {
        User currentUser = authUtil.getCurrentUser();
        UserVinyl userVinyl = userVinylRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vinyl not found."));

        if (!userVinyl.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only view your own vinyls.");
        }

        return toDTO(userVinyl);
    }

    public Long getDiscogsReleaseIdForUserVinyl(Long userVinylId) {
        User currentUser = authUtil.getCurrentUser();
        UserVinyl userVinyl = userVinylRepository.findById(userVinylId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vinyl not found."));

        if (!userVinyl.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only refresh prices for your own vinyls.");
        }

        return userVinyl.getDiscogsRelease().getId();
    }

    public UserVinylResponseDTO addVinyl(UserVinylRequestDTO dto) {
        User currentUser = authUtil.getCurrentUser();

        // Step 1: Get or create the DiscogsRelease
        DiscogsRelease release;

        if (dto.getDiscogsId() != null) {
            // Discogs-backed entry — fetch or reuse from local DB
            release = discogsService.fetchAndSaveRelease(dto.getDiscogsId());

            if (release == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Release not found on Discogs.");
            }

            // Check if user already has this release in their collection
            if (userVinylRepository.existsByUserIdAndDiscogsReleaseId(currentUser.getId(), release.getId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "This release is already in your collection.");
            }

        } else {
            // Manual entry — validate required fields
            if (dto.getTitle() == null || dto.getArtist() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title and artist are required for manual entries.");
            }
            release = discogsService.createManualRelease(dto.getTitle(), dto.getArtist(), dto.getReleaseYear());
        }

        // Step 2: Create the UserVinyl
        UserVinyl userVinyl = new UserVinyl();
        userVinyl.setUser(currentUser);
        userVinyl.setDiscogsRelease(release);
        userVinyl.setCondition(dto.getCondition());
        userVinyl.setNotes(dto.getNotes());
        userVinyl.setAddedAt(LocalDateTime.now());

        return toDTO(userVinylRepository.save(userVinyl));
    }

    public UserVinylResponseDTO updateUserVinyl(Long id, UserVinylRequestDTO dto) {
        User currentUser = authUtil.getCurrentUser();
        UserVinyl userVinyl = userVinylRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vinyl not found."));

        if (!userVinyl.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own vinyls.");
        }

        // Only condition and notes are updatable
        // The release itself doesn't change once added
        userVinyl.setCondition(dto.getCondition());
        userVinyl.setNotes(dto.getNotes());

        return toDTO(userVinylRepository.save(userVinyl));
    }

    public void deleteUserVinyl(Long id) {
        User currentUser = authUtil.getCurrentUser();
        UserVinyl userVinyl = userVinylRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vinyl not found."));

        if (!userVinyl.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own vinyls.");
        }

        userVinylRepository.delete(userVinyl);
    }
}