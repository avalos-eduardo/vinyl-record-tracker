package com.example.vinyl_record_collection_tracker.services;

import com.example.vinyl_record_collection_tracker.dtos.DiscogsSearchResultDTO;
import com.example.vinyl_record_collection_tracker.models.DiscogsMaster;
import com.example.vinyl_record_collection_tracker.models.DiscogsRelease;
import com.example.vinyl_record_collection_tracker.repositories.DiscogsMasterRepository;
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
    private final DiscogsMasterRepository discogsMasterRepository;

    public DiscogsService(WebClient discogsWebClient,
                          DiscogsReleaseRepository discogsReleaseRepository,
                          DiscogsMasterRepository discogsMasterRepository) {
        this.discogsWebClient = discogsWebClient;
        this.discogsReleaseRepository = discogsReleaseRepository;
        this.discogsMasterRepository = discogsMasterRepository;
    }

    // Search Discogs for releases matching a query — unchanged
    public List<DiscogsSearchResultDTO> search(String query, int page) {
        Map<String, Object> body = discogsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/database/search")
                        .queryParam("q", query)
                        .queryParam("type", "release")
                        .queryParam("format", "vinyl")
                        .queryParam("per_page", 10)
                        .queryParam("page", page)
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

            List<Map<String, Object>> formatsArray = (List<Map<String, Object>>) result.getOrDefault("formats", List.of());
            String vinylColor = null;
            List<String> formatDescriptions = List.of();

            if (!formatsArray.isEmpty()) {
                Map<String, Object> primaryFormat = formatsArray.get(0);
                vinylColor = (String) primaryFormat.getOrDefault("text", null);
                formatDescriptions = (List<String>) primaryFormat.getOrDefault("descriptions", List.of());
            }

            String country = (String) result.getOrDefault("country", null);

            results.add(new DiscogsSearchResultDTO(
                    discogsId,
                    releaseTitle,
                    artist,
                    label,
                    genre,
                    format,
                    releaseYear,
                    imageUrl,
                    vinylColor,
                    formatDescriptions,
                    country
            ));
        }

        return results;
    }

    // Fetch a specific release from Discogs by its ID
    public DiscogsRelease fetchAndSaveRelease(String discogsId) {
        // Check if we already have it locally
        Optional<DiscogsRelease> existing = discogsReleaseRepository.findByDiscogsId(discogsId);
        if (existing.isPresent()) return existing.get();

        // Fetch from Discogs API
        Map<String, Object> body = discogsWebClient.get()
                .uri("/releases/" + discogsId)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (body == null) return null;

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

        // format, vinylColor, formatDescriptions all come from formats
        List<Map<String, Object>> formats = (List<Map<String, Object>>) body.getOrDefault("formats", List.of());
        if (!formats.isEmpty()) {
            Map<String, Object> primaryFormat = formats.get(0);

            release.setFormat((String) primaryFormat.getOrDefault("name", null));
            release.setVinylColor((String) primaryFormat.getOrDefault("text", null));

            List<String> descriptions = (List<String>) primaryFormat.getOrDefault("descriptions", List.of());
            release.setFormatDescriptions(descriptions);
        }

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

        // barcode extraction
        List<Map<String, Object>> identifiers = (List<Map<String, Object>>) body.getOrDefault("identifiers", List.of());
        release.setBarcode(extractBarcode(identifiers));

        release.setCountry((String) body.getOrDefault("country", null));

        release.setLastSyncedAt(LocalDateTime.now());

        // Resolve and attach master (from earlier refactor)
        Object masterIdRaw = body.get("master_id");
        DiscogsMaster master;

        if (masterIdRaw != null) {
            String masterIdStr = masterIdRaw.toString();
            master = discogsMasterRepository.findByMasterId(masterIdStr)
                    .orElseGet(() -> fetchAndSaveMaster(masterIdStr, release));
        } else {
            master = createPseudoMaster(release);
        }

        release.setMaster(master);
        return discogsReleaseRepository.save(release);
    }

    // Finds the first valid 12 or 13 digit barcode among "Barcode"-type identifiers
    private String extractBarcode(List<Map<String, Object>> identifiers) {
        for (Map<String, Object> identifier : identifiers) {
            String type = (String) identifier.getOrDefault("type", "");
            if (!"Barcode".equals(type)) continue;

            String value = (String) identifier.getOrDefault("value", "");
            String digitsOnly = value.replaceAll("[^0-9]", "");

            if (digitsOnly.length() == 12 || digitsOnly.length() == 13) {
                return digitsOnly;
            }
        }
        return null;
    }

    // Fetch a master from Discogs and save it — called only when not already in DB
    private DiscogsMaster fetchAndSaveMaster(String masterId, DiscogsRelease release) {
        try {
            Map<String, Object> body = discogsWebClient.get()
                    .uri("/masters/" + masterId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (body == null) return createPseudoMaster(release);

            DiscogsMaster master = new DiscogsMaster();
            master.setMasterId(masterId);
            master.setTitle((String) body.getOrDefault("title", release.getTitle()));

            List<Map<String, Object>> artists = (List<Map<String, Object>>) body.getOrDefault("artists", List.of());
            String artist = artists.isEmpty()
                    ? release.getArtist()
                    : (String) artists.get(0).getOrDefault("name", release.getArtist());
            master.setArtist(artist);

            List<Map<String, Object>> images = (List<Map<String, Object>>) body.getOrDefault("images", List.of());
            String imageUrl = images.isEmpty()
                    ? release.getImageUrl()
                    : (String) images.get(0).getOrDefault("uri", release.getImageUrl());
            master.setImageUrl(imageUrl);

            return discogsMasterRepository.save(master);

        } catch (Exception e) {
            // Master fetch failed — fall back to pseudo-master
            return createPseudoMaster(release);
        }
    }

    // Create or reuse a pseudo-master for releases with no Discogs master_id
    private DiscogsMaster createPseudoMaster(DiscogsRelease release) {
        return discogsMasterRepository
                .findByMasterIdIsNullAndTitleAndArtist(release.getTitle(), release.getArtist())
                .orElseGet(() -> {
                    DiscogsMaster master = new DiscogsMaster();
                    master.setMasterId(null);
                    master.setTitle(release.getTitle());
                    master.setArtist(release.getArtist());
                    master.setImageUrl(release.getImageUrl());
                    return discogsMasterRepository.save(master);
                });
    }

    // Fetch price statistics for a specific release — unchanged
    public Map<String, Object> fetchPriceStatistics(String discogsId) {
        try {
            return discogsWebClient.get()
                    .uri("/marketplace/stats/" + discogsId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (WebClientResponseException e) {
            return null;
        }
    }

    // Create a manual DiscogsRelease with no Discogs ID — updated to attach pseudo-master
    public DiscogsRelease createManualRelease(String title, String artist, Integer releaseYear) {
        DiscogsRelease release = new DiscogsRelease();
        release.setDiscogsId(null);
        release.setTitle(title);
        release.setArtist(artist);
        release.setReleaseYear(releaseYear);
        release.setLastSyncedAt(LocalDateTime.now());

        // Manual releases always get a pseudo-master
        DiscogsMaster master = createPseudoMaster(release);
        release.setMaster(master);

        return discogsReleaseRepository.save(release);
    }
}