package com.example.vinyl_record_collection_tracker.dtos;

import com.example.vinyl_record_collection_tracker.models.VinylCondition;

public class UserVinylRequestDTO {
    private String discogsId; // null if vinyl is manually entered
    private String title; // used for manual entry
    private String artist;         // used for manual entry
    private Integer releaseYear;   // used for manual entry
    private VinylCondition condition;
    private String notes;
    private boolean wishlist = false;

    public boolean isWishlist() { return wishlist; }

    public void setWishlist(boolean wishlist) {
        this.wishlist = wishlist;
    }

    public String getDiscogsId() { return discogsId; }
    public void setDiscogsId(String discogsId) { this.discogsId = discogsId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public VinylCondition getCondition() { return condition; }
    public void setCondition(VinylCondition condition) { this.condition = condition; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
