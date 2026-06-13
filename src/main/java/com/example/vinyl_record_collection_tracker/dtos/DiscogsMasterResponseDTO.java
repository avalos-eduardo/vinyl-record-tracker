package com.example.vinyl_record_collection_tracker.dtos;

public class DiscogsMasterResponseDTO {
    private Long id;
    private String masterId;
    private String title;
    private String artist;
    private String imageUrl;
    private int releaseCount;

    public DiscogsMasterResponseDTO(Long id, String masterId, String title, String artist, String imageUrl, int releaseCount) {
        this.id = id;
        this.masterId = masterId;
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.releaseCount = releaseCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getReleaseCount() {
        return releaseCount;
    }

    public void setReleaseCount(int releaseCount) {
        this.releaseCount = releaseCount;
    }
}
