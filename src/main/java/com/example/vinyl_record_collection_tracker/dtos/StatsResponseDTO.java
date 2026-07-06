package com.example.vinyl_record_collection_tracker.dtos;

import java.util.List;
import java.util.Map;

public class StatsResponseDTO {
    private int totalRecords;
    private long uniqueArtists;
    private String mostCollectedArtist;
    private Map<String, Long> conditionBreakdown;
    private Map<String, Long> decadeBreakdown;

    public StatsResponseDTO(int totalRecords,
                            long uniqueArtists,
                            String mostCollectedArtist,
                            Map<String, Long> conditionBreakdown,
                            Map<String, Long> decadeBreakdown) {
        this.totalRecords = totalRecords;
        this.uniqueArtists = uniqueArtists;
        this.mostCollectedArtist = mostCollectedArtist;
        this.conditionBreakdown = conditionBreakdown;
        this.decadeBreakdown = decadeBreakdown;
    }

    public int getTotalRecords() { return totalRecords; }
    public long getUniqueArtists() { return uniqueArtists; }
    public String getMostCollectedArtist() { return mostCollectedArtist; }
    public Map<String, Long> getConditionBreakdown() { return conditionBreakdown; }
    public Map<String, Long> getDecadeBreakdown() { return decadeBreakdown; }
}