package com.example.vinyl_record_collection_tracker.dtos;

import com.example.vinyl_record_collection_tracker.models.VinylCondition;

import java.time.LocalDateTime;

public class UserVinylResponseDTO {
    private Long id;
    private VinylCondition condition;
    private String notes;
    private LocalDateTime addedAt;
    private DiscogsReleaseResponseDTO release;

    public UserVinylResponseDTO(Long id, VinylCondition condition, String notes, LocalDateTime addedAt, DiscogsReleaseResponseDTO release) {
        this.id = id;
        this.condition = condition;
        this.notes = notes;
        this.addedAt = addedAt;
        this.release = release;
    }
}
