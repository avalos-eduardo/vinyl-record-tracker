package com.example.vinyl_record_collection_tracker.dtos;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;

import java.time.LocalDateTime;
import java.util.List;

public class DiscogsReleaseResponseDTO {
    private Long id;
    private String discogsId;
    private String title;
    private String artist;
    private String label;
    private String genre;
    private String format;
    private Integer releaseYear;
    private String imageUrl;
    private LocalDateTime lastSyncedAt;
    private String vinylColor;
    @ElementCollection
    @CollectionTable(name = "discogs_release_format_descriptions")
    private List<String> formatDescriptions;
    private String barcode;
    private String country;


    public DiscogsReleaseResponseDTO(Long id, String discogsId, String title, String artist, String label, String genre, String format, Integer releaseYear, String imageUrl, LocalDateTime lastSyncedAt, String vinylColor, List<String> formatDescriptions, String barcode, String country) {
        this.id = id;
        this.discogsId = discogsId;
        this.title = title;
        this.artist = artist;
        this.label = label;
        this.genre = genre;
        this.format = format;
        this.releaseYear = releaseYear;
        this.imageUrl = imageUrl;
        this.lastSyncedAt = lastSyncedAt;
        this.vinylColor = vinylColor;
        this.formatDescriptions = formatDescriptions;
        this.barcode = barcode;
        this.country = country;
    }

    public String getCountry() { return country; }
    public String getBarcode() { return barcode; }

    public void setBarcode(String barcode) { this.barcode = barcode; }

    public List<String> getFormatDescriptions() { return formatDescriptions; }

    public void setFormatDescriptions(List<String> formatDescriptions) { this.formatDescriptions = formatDescriptions; }

    public String getVinylColor() { return vinylColor; }

    public void setVinylColor(String vinylColor) { this.vinylColor = vinylColor; }
    public Long getId() { return id; }
    public String getDiscogsId() { return discogsId; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getLabel() { return label; }
    public String getGenre() { return genre; }
    public String getFormat() { return format; }
    public Integer getReleaseYear() { return releaseYear; }
    public String getImageUrl() { return imageUrl; }
    public LocalDateTime getLastSyncedAt() { return lastSyncedAt; }
}
