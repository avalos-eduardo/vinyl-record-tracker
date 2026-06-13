package com.example.vinyl_record_collection_tracker.dtos;

import java.util.List;

public class DiscogsSearchResultDTO {
    private String discogsId;
    private String title;
    private String artist;
    private String label;
    private String genre;
    private String format;
    private Integer releaseYear;
    private String imageUrl;
    private String vinylColor;
    private List<String> formatDescriptions;


    public DiscogsSearchResultDTO(String discogsId, String title, String artist, String label, String genre, String format, Integer releaseYear, String imageUrl, String vinylColor, List<String> formatDescriptions) {
        this.discogsId = discogsId;
        this.title = title;
        this.artist = artist;
        this.label = label;
        this.genre = genre;
        this.format = format;
        this.releaseYear = releaseYear;
        this.imageUrl = imageUrl;
        this.vinylColor = vinylColor;
        this.formatDescriptions = formatDescriptions;
    }

    public String getDiscogsId() { return discogsId; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getLabel() { return label; }
    public String getGenre() { return genre; }
    public String getFormat() { return format; }
    public Integer getReleaseYear() { return releaseYear; }
    public String getImageUrl() { return imageUrl; }

    public List<String> getFormatDescriptions() { return formatDescriptions; }

    public void setFormatDescriptions(List<String> formatDescriptions) { this.formatDescriptions = formatDescriptions; }

    public String getVinylColor() { return vinylColor; }

    public void setVinylColor(String vinylColor) { this.vinylColor = vinylColor; }
}
