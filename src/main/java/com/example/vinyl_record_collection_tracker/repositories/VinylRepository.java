package com.example.vinyl_record_collection_tracker.repositories;

import com.example.vinyl_record_collection_tracker.models.Vinyl;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VinylRepository extends JpaRepository<Vinyl, Long> {

    List<Vinyl> findByUserId(Long userId);
}
