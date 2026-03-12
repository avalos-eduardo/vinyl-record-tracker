package com.example.vinyl_record_collection_tracker.dtos;

public class VinylResponseDTO {
    private Long id;
    private String title;
    private String artist;
    private Integer releaseYear;
    private Long userId;

    public VinylResponseDTO(Long id, String title, String artist, Integer releaseYear, Long userId) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.releaseYear = releaseYear;
        this.userId = userId;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public Integer getReleaseYear() { return releaseYear; }
    public Long getUserId() { return userId; }
}
