package com.example.vinyl_record_collection_tracker.repositories;

import com.example.vinyl_record_collection_tracker.models.DiscogsMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscogsMasterRepository extends JpaRepository<DiscogsMaster, Long> {
    Optional<DiscogsMaster> findByMasterId(String masterId);
    Optional<DiscogsMaster> findByMasterIdIsNullAndTitleAndArtist(String title, String artist);

}