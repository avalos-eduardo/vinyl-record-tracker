package com.example.vinyl_record_collection_tracker.models;

import jakarta.persistence.*;

@Entity
public class Vinyl {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String artist;
    private Integer releaseYear;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // foreign key pointing to User
    private User user;

    //Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public Integer getYear() { return releaseYear; }
    public void setYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
