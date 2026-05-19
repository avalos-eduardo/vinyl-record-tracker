package com.example.vinyl_record_collection_tracker.dtos;

import java.time.LocalDateTime;

public class PriceHistoryResponseDTO {
    private Long id;
    private Double lowestPrice;
    private Double medianPrice;
    private Double highestPrice;
    private LocalDateTime recordedAt;

    public PriceHistoryResponseDTO(Long id, Double lowestPrice, Double medianPrice, Double highestPrice, LocalDateTime recordedAt) {
        this.id = id;
        this.lowestPrice = lowestPrice;
        this.medianPrice = medianPrice;
        this.highestPrice = highestPrice;
        this.recordedAt = recordedAt;
    }

    public Long getId() { return id; }
    public Double getLowestPrice() { return lowestPrice; }
    public Double getMedianPrice() { return medianPrice; }
    public Double getHighestPrice() { return highestPrice; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
}
