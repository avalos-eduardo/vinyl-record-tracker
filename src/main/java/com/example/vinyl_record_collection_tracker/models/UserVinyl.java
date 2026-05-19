package com.example.vinyl_record_collection_tracker.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UserVinyl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "discogs_release_id", nullable = false)
    private DiscogsRelease discogsRelease;

    @Enumerated(EnumType.STRING)
    private VinylCondition condition;

    private String notes;

    private LocalDateTime addedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public DiscogsRelease getDiscogsRelease() { return discogsRelease; }
    public void setDiscogsRelease(DiscogsRelease discogsRelease) { this.discogsRelease = discogsRelease; }

    public VinylCondition getCondition() { return condition; }
    public void setCondition(VinylCondition condition) { this.condition = condition; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}
