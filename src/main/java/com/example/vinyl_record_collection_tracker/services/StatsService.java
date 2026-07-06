package com.example.vinyl_record_collection_tracker.services;

import com.example.vinyl_record_collection_tracker.dtos.StatsResponseDTO;
import com.example.vinyl_record_collection_tracker.models.User;
import com.example.vinyl_record_collection_tracker.models.UserVinyl;
import com.example.vinyl_record_collection_tracker.repositories.UserVinylRepository;
import com.example.vinyl_record_collection_tracker.security.AuthUtil;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatsService {

    private final UserVinylRepository userVinylRepository;
    private final AuthUtil authUtil;

    public StatsService(UserVinylRepository userVinylRepository, AuthUtil authUtil) {
        this.userVinylRepository = userVinylRepository;
        this.authUtil = authUtil;
    }

    public StatsResponseDTO getStatsForCurrentUser() {
        User currentUser = authUtil.getCurrentUser();

        List<UserVinyl> collection = userVinylRepository
                .findByUserIdAndWishlistFalse(currentUser.getId());

        int totalRecords = collection.size();

        // Unique artists
        long uniqueArtists = collection.stream()
                .map(v -> v.getDiscogsRelease().getArtist())
                .distinct()
                .count();

        // Most collected artist
        String mostCollectedArtist = collection.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getDiscogsRelease().getArtist(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Condition breakdown
        Map<String, Long> conditionBreakdown = collection.stream()
                .filter(v -> v.getCondition() != null)
                .collect(Collectors.groupingBy(
                        v -> v.getCondition().name(),
                        Collectors.counting()
                ));

        // Decade breakdown
        Map<String, Long> decadeBreakdown = collection.stream()
                .filter(v -> v.getDiscogsRelease().getReleaseYear() != null)
                .collect(Collectors.groupingBy(
                        v -> {
                            int year = v.getDiscogsRelease().getReleaseYear();
                            int decade = (year / 10) * 10;
                            return decade + "s";
                        },
                        Collectors.counting()
                ));

        return new StatsResponseDTO(
                totalRecords,
                uniqueArtists,
                mostCollectedArtist,
                conditionBreakdown,
                decadeBreakdown
        );
    }
}