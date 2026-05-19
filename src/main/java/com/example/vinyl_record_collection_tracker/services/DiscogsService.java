package com.example.vinyl_record_collection_tracker.services;

import com.example.vinyl_record_collection_tracker.dtos.DiscogsSearchResultDTO;
import com.example.vinyl_record_collection_tracker.models.DiscogsRelease;
import com.example.vinyl_record_collection_tracker.repositories.DiscogsReleaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DiscogsService {

    private final WebClient discogsWebClient;
    private final DiscogsReleaseRepository discogsReleaseRepository;

    public DiscogsService(WebClient discogsWebClient,
                          DiscogsReleaseRepository discogsReleaseRepository) {
        this.discogsWebClient = discogsWebClient;
        this.discogsReleaseRepository = discogsReleaseRepository;
    }

    // Search Discogs for releases matching a query
    public List<DiscogsSearchResultDTO> search(String query) {
        Map<String, Object> body = discogsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/database/search")
                        .queryParam("q", query)
                        .queryParam("type", "release")
                        .queryParam("per_page", 10)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<DiscogsSearchResultDTO> results = new ArrayList<>();

        if (body == null || !body.containsKey("results")) {
            return results;
        }

        List<Map<String, Object>> rawResults = (List<Map<String, Object>>) body.get("results");

        for (Map<String, Object> result : rawResults) {
            String discogsId = String.valueOf(result.get("id"));
            String title = (String) result.getOrDefault("title", "Unknown Title");

            String artist = "Unknown Artist";
            String releaseTitle = title;
            if (title.contains(" - ")) {
                String[] parts = title.split(" - ", 2);
                artist = parts[0].trim();
                releaseTitle = parts[1].trim();
            }

            List<String> genres = (List<String>) result.getOrDefault("genre", List.of());
            String genre = genres.isEmpty() ? null : genres.get(0);

            List<String> formats = (List<String>) result.getOrDefault("format", List.of());
            String format = formats.isEmpty() ? null : formats.get(0);

            List<String> labels = (List<String>) result.getOrDefault("label", List.of());
            String label = labels.isEmpty() ? null : labels.get(0);

            Integer releaseYear = null;
            Object year = result.get("year");
            if (year != null) {
                try {
                    releaseYear = Integer.parseInt(year.toString());
                } catch (NumberFormatException e) {
                    releaseYear = null;
                }
            }

            String imageUrl = (String) result.getOrDefault("cover_image", null);

            results.add(new DiscogsSearchResultDTO(
                    discogsId,
                    releaseTitle,
                    artist,
                    label,
                    genre,
                    format,
                    releaseYear,
                    imageUrl
            ));
        }

        return results;
    }

    // Fetch a specific release from Discogs by its ID
    public DiscogsRelease fetchAndSaveRelease(String discogsId) {
        // Check if we already have it locally
        Optional<DiscogsRelease> existing = discogsReleaseRepository.findByDiscogsId(discogsId);
        if (existing.isPresent()) {
            return existing.get();
        }

        // Fetch from Discogs API
        Map<String, Object> body = discogsWebClient.get()
                .uri("/releases/" + discogsId)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (body == null) {
            return null;
        }

        DiscogsRelease release = new DiscogsRelease();
        release.setDiscogsId(discogsId);
        release.setTitle((String) body.getOrDefault("title", "Unknown Title"));

        List<Map<String, Object>> artists = (List<Map<String, Object>>) body.getOrDefault("artists", List.of());
        String artist = artists.isEmpty() ? "Unknown Artist" : (String) artists.get(0).getOrDefault("name", "Unknown Artist");
        release.setArtist(artist);

        List<Map<String, Object>> labels = (List<Map<String, Object>>) body.getOrDefault("labels", List.of());
        String label = labels.isEmpty() ? null : (String) labels.get(0).getOrDefault("name", null);
        release.setLabel(label);

        List<String> genres = (List<String>) body.getOrDefault("genres", List.of());
        release.setGenre(genres.isEmpty() ? null : genres.get(0));

        List<Map<String, Object>> formats = (List<Map<String, Object>>) body.getOrDefault("formats", List.of());
        String format = formats.isEmpty() ? null : (String) formats.get(0).getOrDefault("name", null);
        release.setFormat(format);

        Object year = body.get("year");
        if (year != null) {
            try {
                release.setReleaseYear(Integer.parseInt(year.toString()));
            } catch (NumberFormatException e) {
                release.setReleaseYear(null);
            }
        }

        List<Map<String, Object>> images = (List<Map<String, Object>>) body.getOrDefault("images", List.of());
        String imageUrl = images.isEmpty() ? null : (String) images.get(0).getOrDefault("uri", null);
        release.setImageUrl(imageUrl);

        release.setLastSyncedAt(LocalDateTime.now());

        return discogsReleaseRepository.save(release);
    }

    // Fetch price statistics for a specific release
    public Map<String, Object> fetchPriceStatistics(String discogsId) {
        try {
            return discogsWebClient.get()
                    .uri("/marketplace/stats/" + discogsId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (WebClientResponseException e) {
            // Price data may not be available for all releases
            return null;
        }
    }

    // Create a manual DiscogsRelease with no Discogs ID
    public DiscogsRelease createManualRelease(String title, String artist, Integer releaseYear) {
        DiscogsRelease release = new DiscogsRelease();
        release.setDiscogsId(null);
        release.setTitle(title);
        release.setArtist(artist);
        release.setReleaseYear(releaseYear);
        release.setLastSyncedAt(LocalDateTime.now());
        return discogsReleaseRepository.save(release);
    }
}