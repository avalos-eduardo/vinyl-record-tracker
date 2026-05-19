package com.example.vinyl_record_collection_tracker.repositories;

import com.example.vinyl_record_collection_tracker.models.UserVinyl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserVinylRepository extends JpaRepository<UserVinyl, Long> {
    List<UserVinyl> findByUserId(Long userId);
    boolean existsByUserIdAndDiscogsReleaseId(Long userId, Long discogsReleaseId);
}
