package com.example.vinyl_record_collection_tracker.dtos;

public class VinylRequestDTO {
    private String title;
    private String artist;
    private Integer releaseYear;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

}
