package com.example.vinyl_record_collection_tracker.services;

import com.example.vinyl_record_collection_tracker.dtos.PriceHistoryResponseDTO;
import com.example.vinyl_record_collection_tracker.models.DiscogsRelease;
import com.example.vinyl_record_collection_tracker.models.PriceHistory;
import com.example.vinyl_record_collection_tracker.repositories.DiscogsReleaseRepository;
import com.example.vinyl_record_collection_tracker.repositories.PriceHistoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;
    private final DiscogsReleaseRepository discogsReleaseRepository;
    private final DiscogsService discogsService;

    public PriceHistoryService(PriceHistoryRepository priceHistoryRepository,
                               DiscogsReleaseRepository discogsReleaseRepository,
                               DiscogsService discogsService) {
        this.priceHistoryRepository = priceHistoryRepository;
        this.discogsReleaseRepository = discogsReleaseRepository;
        this.discogsService = discogsService;
    }

    private PriceHistoryResponseDTO toDTO(PriceHistory priceHistory) {
        return new PriceHistoryResponseDTO(
                priceHistory.getId(),
                priceHistory.getLowestPrice(),
                priceHistory.getMedianPrice(),
                priceHistory.getHighestPrice(),
                priceHistory.getRecordedAt()
        );
    }

    public List<PriceHistoryResponseDTO> getPriceHistory(Long discogsReleaseId) {
        // Verify the release exists
        discogsReleaseRepository.findById(discogsReleaseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Release not found."));

        return priceHistoryRepository
                .findByDiscogsReleaseIdOrderByRecordedAtAsc(discogsReleaseId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public void recordPriceSnapshot(DiscogsRelease release) {
        // Skip manual entries with no Discogs ID
        if (release.getDiscogsId() == null) {
            return;
        }

        // Fetch price data from Discogs
        Map<String, Object> priceData = discogsService.fetchPriceStatistics(release.getDiscogsId());

        // Skip if no price data available for this release
        if (priceData == null) {
            return;
        }

        // Extract price statistics
        Double lowestPrice = extractPrice(priceData, "lowest_price");
        Double medianPrice = extractPrice(priceData, "median");
        Double highestPrice = extractPrice(priceData, "highest_price");

        // Skip if all prices are null — no meaningful data to store
        if (lowestPrice == null && medianPrice == null && highestPrice == null) {
            return;
        }

        PriceHistory snapshot = new PriceHistory();
        snapshot.setDiscogsRelease(release);
        snapshot.setLowestPrice(lowestPrice);
        snapshot.setMedianPrice(medianPrice);
        snapshot.setHighestPrice(highestPrice);
        snapshot.setRecordedAt(LocalDateTime.now());

        priceHistoryRepository.save(snapshot);
    }

    // Helper to safely extract price values from the Discogs response
    private Double extractPrice(Map<String, Object> priceData, String key) {
        try {
            Object value = priceData.get(key);
            if (value == null) return null;

            // Discogs returns prices nested in an object like
            // { "value": 12.99, "currency": "USD" }
            if (value instanceof Map) {
                Object nestedValue = ((Map<?, ?>) value).get("value");
                if (nestedValue == null) return null;
                return Double.parseDouble(nestedValue.toString());
            }

            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void recordAllPrices(List<DiscogsRelease> releases) {
        for (DiscogsRelease release : releases) {
            try {
                recordPriceSnapshot(release);
            } catch (Exception e) {
                // Log and continue — one failure shouldn't stop the entire job
                System.err.println("Failed to record price for release "
                        + release.getDiscogsId() + ": " + e.getMessage());
            }
        }
    }

    public PriceHistoryResponseDTO refreshPrice(Long discogsReleaseId) {
        DiscogsRelease release = discogsReleaseRepository.findById(discogsReleaseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Release not found."));

        if (release.getDiscogsId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price refresh is not available for manually added releases.");
        }

        recordPriceSnapshot(release);

        return priceHistoryRepository
                .findByDiscogsReleaseIdOrderByRecordedAtAsc(discogsReleaseId)
                .stream()
                .reduce((first, second) -> second)
                .map(this::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No price data available for this release."));
    }
}