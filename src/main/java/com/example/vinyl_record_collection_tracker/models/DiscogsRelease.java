package com.example.vinyl_record_collection_tracker.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class DiscogsRelease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = true)
    private String discogsId;

    @ManyToOne
    @JoinColumn(name = "discogs_master_id")
    private DiscogsMaster master;

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

    @OneToMany(mappedBy = "discogsRelease", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserVinyl> userVinyls;

    @OneToMany(mappedBy = "discogsRelease", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PriceHistory> priceHistories;

    private String country;

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    public String getVinylColor() { return vinylColor; }

    public void setVinylColor(String vinylColor) { this.vinylColor = vinylColor; }

    public List<String> getFormatDescriptions() { return formatDescriptions; }

    public void setFormatDescriptions(List<String> formatDescriptions) { this.formatDescriptions = formatDescriptions; }

    public String getBarcode() { return barcode; }

    public void setBarcode(String barcode) { this.barcode = barcode; }

    public DiscogsMaster getMaster() { return master; }

    public void setMaster(DiscogsMaster master)  { this.master = master; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDiscogsId() { return discogsId; }
    public void setDiscogsId(String discogsId) { this.discogsId = discogsId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getLastSyncedAt() { return lastSyncedAt; }
    public void setLastSyncedAt(LocalDateTime lastSyncedAt) { this.lastSyncedAt = lastSyncedAt; }

    public List<UserVinyl> getUserVinyls() { return userVinyls; }
    public void setUserVinyls(List<UserVinyl> userVinyls) { this.userVinyls = userVinyls; }

    public List<PriceHistory> getPriceHistories() { return priceHistories; }
    public void setPriceHistories(List<PriceHistory> priceHistories) { this.priceHistories = priceHistories; }
}
