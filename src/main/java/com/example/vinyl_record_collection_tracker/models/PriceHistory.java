package com.example.vinyl_record_collection_tracker.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="discogs_release_id", nullable = false)
    private DiscogsRelease discogsRelease;

    private Double lowestPrice;
    private Double medianPrice;
    private Double highestPrice;
    private LocalDateTime recordedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public DiscogsRelease getDiscogsRelease() { return discogsRelease; }
    public void setDiscogsRelease(DiscogsRelease discogsRelease) { this.discogsRelease = discogsRelease; }

    public Double getLowestPrice() { return lowestPrice; }
    public void setLowestPrice(Double lowestPrice) { this.lowestPrice = lowestPrice; }

    public Double getMedianPrice() { return medianPrice; }
    public void setMedianPrice(Double medianPrice) { this.medianPrice = medianPrice; }

    public Double getHighestPrice() { return highestPrice; }
    public void setHighestPrice(Double highestPrice) { this.highestPrice = highestPrice; }

    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}
