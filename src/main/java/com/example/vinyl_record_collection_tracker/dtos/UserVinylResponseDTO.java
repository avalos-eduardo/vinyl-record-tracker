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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VinylCondition getCondition() {
        return condition;
    }

    public void setCondition(VinylCondition condition) {
        this.condition = condition;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public DiscogsReleaseResponseDTO getRelease() {
        return release;
    }

    public void setRelease(DiscogsReleaseResponseDTO release) {
        this.release = release;
    }
}
