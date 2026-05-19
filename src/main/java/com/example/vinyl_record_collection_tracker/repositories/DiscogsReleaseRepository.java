package com.example.vinyl_record_collection_tracker.repositories;

import com.example.vinyl_record_collection_tracker.models.DiscogsRelease;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscogsReleaseRepository extends JpaRepository<DiscogsRelease, Long> {
    Optional<DiscogsRelease> findByDiscogsId(String discogsId);
}
